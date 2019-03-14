package ch.epfl.imhof.osm;									//MODIF

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import java.util.List;
import java.util.Set;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.Graph;
import ch.epfl.imhof.Graph.Builder;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.ClosedPolyLine;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.osm.OSMRelation.Member.Type;

import ch.epfl.imhof.projection.Projection;

/**
 * @author Pierre Gabioud (247216)
 * @author Corto Callerisa (251769)
 *
 * Représente un convertisseur de données OpenStreetMap en carte.
 */
public final class OSMToGeoTransformer {
	// la projection de la carte.														 
    private final Projection projection;     
    
    // le set des attributs associés à un chemin fermé avec une aire.					 
    private final Set<String> att = new HashSet<>(Arrays.asList("aeroway",    
            "amenity", "building", "harbour", "historic", "landuse", "leisure",
            "man_made", "military", "natural", "office", "place", "power",
            "public_transport", "shop", "sport", "tourism", "water",
            "waterway", "wetland"));

    // set des attributs des polylignes à garder.										 
    private final Set<String> attLine = new HashSet<>(Arrays.asList("bridge", 
            "highway", "layer", "man_made", "railway", "tunnel", "waterway"));

    // set des attributs des polygones à garder.										 
    private final Set<String> attGone = new HashSet<>(Arrays.asList("building", 
            "landuse", "layer", "leisure", "natural", "waterway"));

    // le graphe.																			 
    private Graph<OSMNode> finalG; 

    /**
     * @param projection
     *            la projection utilisée par le convertisseur OpenStreetMap en géometrie
     */
    public OSMToGeoTransformer(Projection projection) {
        this.projection = projection;
    }

    /**
     * @param map
     *            la carte OSM à convertir
     * 
     * @return la carte géometrique convertie à partir de la carte OpenStreetMap
     */
    public Map transform(OSMMap map) {
        Map.Builder newMap = new Map.Builder();
        List<OSMWay> ways = map.ways();
        List<OSMRelation> relations = map.relations();
        List<Attributed<Polygon>> polygons;

        // traitement des chemins															 
        for (OSMWay way : ways) {   
            if (way.isClosed()) {
                if (haveSurface(way)) {
                    Attributes filtredAttsGone = way.attributes().keepOnlyKeys(
                            attGone);
                    if (!filtredAttsGone.isEmpty()) {
                        ClosedPolyLine coquille = wayToClosed(way);
                        Attributed<Polygon> polygon = new Attributed<>(
                                new Polygon(coquille,
                                        new ArrayList<ClosedPolyLine>()),
                                filtredAttsGone);
                        newMap.addPolygon(polygon);
                    }
                    // polylignes fermées sans surface
                } else {
                    Attributes filtredAttsLine = way.attributes().keepOnlyKeys(
                            attLine);
                    if (!filtredAttsLine.isEmpty()) {
                        PolyLine.Builder polyBuild = new PolyLine.Builder();
                        for (OSMNode node : way.nodes()) {
                            polyBuild.addPoint(projection.project(node
                                    .position()));
                        }
                        newMap.addPolyLine(new Attributed<PolyLine>(polyBuild
                                .buildClosed(), filtredAttsLine));

                    }
                }

            }

            else {
                Attributes filtredAttsLine = way.attributes().keepOnlyKeys(
                        attLine);
                if (!filtredAttsLine.isEmpty()) {
                    PolyLine.Builder polyBuild = new PolyLine.Builder();
                    for (OSMNode node : way.nodes()) {
                        polyBuild.addPoint(projection.project(node.position()));
                    }
                    newMap.addPolyLine(new Attributed<PolyLine>(polyBuild
                            .buildOpen(), filtredAttsLine));
                }

            }

        }
        // traitement des relations
        for (OSMRelation r : relations) { 
                Attributes attsPolygon = r.attributes().keepOnlyKeys(attGone);
                // si la relation possède au moins un attributs pertinent   
                if (!attsPolygon.isEmpty()) { 
                    polygons = assemblePolygon(r, attsPolygon);
                    for (Attributed<Polygon> gone : polygons) {
                        newMap.addPolygon(gone);
                    }
                }
        }
        return newMap.build();
    }

    /**
     * @param relation
     *            la relation dans laquelle on cherche les anneaux
     * 
     * @param role
     *            le rôle de la relation donnée
     * 
     * @return l'ensemble des anneaux de la relation ayant le rôle
     *         specifié, ou une liste vide si le calcul des anneaux
     *         échoue.
     */
    private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role) {
        List<ClosedPolyLine> rings = new ArrayList<>();
        List<OSMWay> ways = new ArrayList<>();
        Graph.Builder<OSMNode> graphB = new Builder<>();

        for (OSMRelation.Member r : relation.members()) {
            if (r.type().equals(Type.WAY) && r.role().equals(role)) {
                ways.add((OSMWay) r.member());
            }
        }

        for (OSMWay w1 : ways) {
            for (int j = 0; j < w1.nodes().size(); j++) {
                graphB.addNode(w1.nodes().get(j));
                if (j > 0) {
                    graphB.addEdge(w1.nodes().get(j - 1), w1.nodes().get(j));
                }
            }
        }
        finalG = graphB.build();

        for (OSMNode node : finalG.nodes()) {
        	// test si les voisins sont corrects														 
            if (finalG.neighborsOf(node).size() != 2) { 
                return rings;
            }
        }
                
        Iterator<OSMNode> i = finalG.nodes().iterator();
        Set<OSMNode> usedNodes = new HashSet<>();
        PolyLine.Builder polyL = new PolyLine.Builder();
        
        // forme les anneaux																			 
        while (i.hasNext()) { 
            OSMNode nd = i.next();
            if (!usedNodes.contains(nd)) {
                polyL.addPoint(projection.project(nd.position()));
                usedNodes.add(nd);
                Set<OSMNode> neighbors = new HashSet<>(finalG.neighborsOf(nd));
                Iterator<OSMNode> neighborsIt = neighbors.iterator();
                do {
                    OSMNode node = neighborsIt.next();
                    if (!usedNodes.contains(node)) {
                        usedNodes.add(node);
                        polyL.addPoint(projection.project(node.position()));
                        neighbors = new HashSet<>(finalG.neighborsOf(node));
                        neighborsIt = new HashSet<>(neighbors).iterator();
                    }
                } while (neighborsIt.hasNext());
                rings.add(polyL.buildClosed());
                polyL = new PolyLine.Builder();
            }        }

        return rings;

    }

    /**
     * @param way
     *            le chemin dont on test la possession d'une surface
     * 
     * @return VRAI si le chemin forme une surface, FAUX sinon
     */
    private boolean haveSurface(OSMWay way) {
        for (String attrib : att) {
        	// Un chemin à une surface s'il posséde un attribut impliquant une surface.				 
            if (way.hasAttribute(attrib)) { 
                return true;
            }
        }
        // ou s'il a l'attribut "area" avec la valeur "1", "true" ou "yes"
        if (way.hasAttribute("area")) { 
            return (way.attributeValue("area").equals("1")
                    || way.attributeValue("area").equals("true") || way
                    .attributeValue("area").equals("yes"));
        }
        return false;
    }

    /**
     * @param relation
     *            la relation où l'on va chercher des polygones
     * 
     * @param attributes
     *            les attributs à affecter aux polygones trouvés
     * 
     * @return la liste des polygones attribués de la relation, en leur
     *         attachant les attributs respectifs
     */
 
    private List<Attributed<Polygon>> assemblePolygon(OSMRelation relation, Attributes attributes) {
        List<Attributed<Polygon>> polygons = new ArrayList<>();
        List<ClosedPolyLine> outer = ringsForRole(relation, "outer");
        List<ClosedPolyLine> inner = ringsForRole(relation, "inner");
        sortAscend(outer);
        sortAscend(inner);

        // Traitement de chaque anneaux extérieur.														 
        for (ClosedPolyLine closedPoly : outer) {   
            List<ClosedPolyLine> holes = new ArrayList<>();
            int j = 0;
            // ajout des anneaux correspondants à l'anneau extérieur									
            do {              
                if (inner.size() != 0 && closedPoly.containsPoint(inner.get(j).firstPoint()) && closedPoly.area()>inner.get(j).area()) { //verif d'aire ?? 
                    holes.add(inner.get(j));
                }
                j++;
            } while (j < inner.size()); 
            inner.removeAll(holes);
            polygons.add(new Attributed<Polygon>(new Polygon(closedPoly, holes),attributes));
            holes.clear();
        }
        
        return polygons;
    }
 

    /**
     * Méthode triant les aires des ClosedPolyLines dans l'ordre croissant.
    
     * @param poly
     *            la liste de ClosedPolyLine
     * 
     */
    private static void sortAscend(List<ClosedPolyLine> poly) {
        Collections.sort(poly,
                (ClosedPolyLine a, ClosedPolyLine b) -> ((Double) a.area()).compareTo(b.area()));
    }

    /**
     * Méthode privée facilitant la création d'une ClosedPolyLine à partir d'un chemin.
     * 
     * @param way
     *            le chemin à convertir
     * 
     * @return une ClosedPolyLine à partir d'un chemin
     */
    private ClosedPolyLine wayToClosed(OSMWay way) {
        PolyLine.Builder poly = new PolyLine.Builder();
        for (OSMNode node : way.nonRepeatingNodes()) {
            Point point = projection.project(node.position());
            poly.addPoint(point);
        }
        return poly.buildClosed();
    }

}
