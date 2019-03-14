package ch.epfl.imhof.osm;												

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import ch.epfl.imhof.PointGeo;

/**
 * @author Pierre Gabioud (247216)
 * @author Corto Callerisa (251769)
 * 
 *         Construit une carte OpenStreetMap à partir de données stockées dans
 *         un fichier au format OpenStreetMap.
 */
public final class OSMMapReader {
    private final static String NODE_NAME = "node", WAY_NAME = "way", RELATION_NAME = "relation";
    private final static String TAG_NAME = "tag", ND_NAME = "nd", MEMBER_NAME = "member";
    private final static String REF_NAME = "ref", ROLE_NAME = "role", ID_NAME = "id" ;
    private final static String V_NAME = "v", K_NAME = "k";
    private final static String LAT_NAME = "lat", LON_NAME = "lon";

    

    /**
     * Le constructeur par défaut de la classe OSMMapReader.
     */
    private OSMMapReader() {
    };

    /**
     * @param fileName
     *            le nom du fichier contenant la carte OSM
     * 
     * @param unGZip
     *            boolean valant VRAI si le fichier est compresse en format
     *            GZip, FAUX sinon
     * 
     * @return la carte OSM contenue dans le fichier identifié par fileName
     * 
     * @throws IOException
     *             si il y a des erreurs d'Entrée/Sortie autre qu'un mauvais
     *             format (lance l'exception SAXException le cas échéant), p.ex.
     *             si le fichier n'existe pas
     * 
     * @throws SAXException
     *             si il y a une erreur dans le format du fichier
     */
    public static OSMMap readOSMFile(String fileName, boolean unGZip)
            throws IOException, SAXException {

        OSMMap.Builder map = new OSMMap.Builder();

        InputStream i = new BufferedInputStream(new FileInputStream(fileName));
        // dézippage du fichier si nécéssaire												 
        if (unGZip) {
            i = new GZIPInputStream(i);    
        }
        try (InputStream j = i) {
            XMLReader r = XMLReaderFactory.createXMLReader();    
            // creation du gestionnaire de contenu											 
            r.setContentHandler(new DefaultHandler() { 
                public OSMNode.Builder node = null;
                OSMWay.Builder way = null;
                OSMRelation.Builder relation = null;

                /**
                 * 
                 * Méthode du gestionnaire de contenu traitant la rencontre d'une balise
                 * ouvrante.
                 * 
                 * @param qname
                 *            le nom de l'élement rencontré
                 * 
                 * @param atts
                 *            les attributs de l'élémemt rencontré
                 * 
                 */
                @Override
                // throws SAXException si il y a une erreur dans le format du fichier			 
                public void startElement(String uri, String lName,
                        String qName, Attributes atts) throws SAXException { 
                    
                    // traitement de l'élément en fonction de son nom							 
                    switch (qName) {
                    // traitement d'un noeud													 
                    case NODE_NAME:    
                        parseNode(atts);
                        break;
                    // traitement d'un chemin													     
                    case WAY_NAME: 
                        parseWay(atts);
                        break;
                    // traitement d'une relation												 
                    case RELATION_NAME:  
                        parseRelation(atts);
                        break;
                    // traitement d'un tag														 
                    case TAG_NAME:                                    
                        parseTag(atts); 
                        break;
                    // traitement d'un nd    
                    case ND_NAME:                                      
                        parseNd(map, atts);
                        break;
                    // traitement d'un member															 
                    case MEMBER_NAME:                                  
                        parseMember(map, atts);
                        break;
                    }
                }

                private void parseNd(OSMMap.Builder map, Attributes atts) {
                    if (map.nodeForId(Long.parseLong(atts.getValue(REF_NAME))) != null) {
                    	// nd fait partie des attributs de way donc on l'ajoute						 
                        way.addNode(map.nodeForId(Long.parseLong(atts.getValue(REF_NAME))));  
                    } else {
                        way.setIncomplete();
                    }
                }

                private void parseRelation(Attributes atts) {
                    relation = new OSMRelation.Builder(Long.parseLong(atts
                            .getValue(ID_NAME)));
                }

                private void parseWay(Attributes atts) {
                    way = new OSMWay.Builder(Long.parseLong(atts
                            .getValue(ID_NAME)));
                }

                private void parseNode(Attributes atts) {
                    double lon = Math.toRadians(Double.parseDouble(atts
                            .getValue(LON_NAME)));                                 
                    double lat = Math.toRadians(Double.parseDouble(atts
                            .getValue(LAT_NAME)));                                 
                    node = new OSMNode.Builder(Long.parseLong(atts
                            .getValue(ID_NAME)), new PointGeo(lon, lat));        
                }

                private void parseTag(Attributes atts) {
                    if (way == null && relation != null) {
                        relation.setAttribute(atts.getValue(K_NAME), atts.getValue(V_NAME));
                    } else if (way != null && relation == null) {
                        way.setAttribute(atts.getValue(K_NAME), atts.getValue(V_NAME));
                    } else if (node != null && way == null
                            && relation == null) {
                        node.setAttribute(atts.getValue(K_NAME), atts.getValue(V_NAME));
                    }
                }

                private void parseMember(OSMMap.Builder map, Attributes atts) {
                    switch (atts.getValue("type")) {
                    // member de type node															 
                    case NODE_NAME: 
                    	// cas ou une identité non définie dans le fichier est détectée				 
                        parseMemberNode(map, atts);
                        break;
                    // member de type way															 
                    case WAY_NAME: 
                        parseMemberWay(map, atts);
                        break;
                    // member de type relation    													 
                    case RELATION_NAME:  
                        parseMemberRelation(map, atts);
                        break;
                    }
                }

                private void parseMemberRelation(OSMMap.Builder map,
                        Attributes atts) {
                    if (map.relationForId(Long.parseLong(atts
                            .getValue(REF_NAME))) == null) {
                        relation.setIncomplete();
                    } else {
                        relation.addMember(
                                OSMRelation.Member.Type.RELATION, atts
                                        .getValue(ROLE_NAME),
                                map.relationForId(Long.parseLong(atts
                                        .getValue(REF_NAME))));
                    }
                }

                private void parseMemberWay(OSMMap.Builder map, Attributes atts) {
                    if (map.wayForId(Long.parseLong(atts.getValue(REF_NAME))) == null) {
                        relation.setIncomplete();
                    } else {
                        relation.addMember(OSMRelation.Member.Type.WAY,
                                atts.getValue(ROLE_NAME), map
                                .wayForId(Long.parseLong(atts
                                .getValue(REF_NAME))));
                    }
                }

                private void parseMemberNode(OSMMap.Builder map, Attributes atts) {
                    if (map.nodeForId(Long.parseLong(atts  
                            .getValue(REF_NAME))) == null) {
                        relation.setIncomplete();
                    } else {
                        relation.addMember(
                                OSMRelation.Member.Type.NODE, atts
                                        .getValue(ROLE_NAME), map
                                        .nodeForId(Long.parseLong(atts
                                        .getValue(REF_NAME))));
                    }
                }

                /**
                 * Méthode du gestionnaire de contenu traitant la rencontre d'une balise
                 * fermante.
                 * 
                 * @param qname
                 *            le nom de lélément rencontré
                 * 
                 * @param atts
                 *            les attributs de l'élémemt rencontr 
                 */
                @Override
                public void endElement(String uri, String lName, String qName) {
                	// ajout des attributs lus dans la carte 											 
                    switch (qName) { 
                    case NODE_NAME:
                        map.addNode(node.build());
                        node = null;
                        break;
                    case WAY_NAME:
                        if (!way.isIncomplete()) {
                            map.addWay(way.build());
                        }
                        way = null;
                        break;
                    case RELATION_NAME:
                        if (!relation.isIncomplete()) {
                            map.addRelation(relation.build());
                        }
                        relation = null;
                        break;

                    }
                }
            });
            // lecture du fichier																		 
            r.parse(new InputSource(j)); 
        }

        return map.build();
    }
}
