package ch.epfl.imhof.osm;										//MODIF

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * @author Pierre Gabioud  (247216)
 * @author Corto Callerisa (251769)
 *
 *  Représente une carte openStreetMap.
 */
public final class OSMMap {
    private final List<OSMWay> ways;
    private final List<OSMRelation> relations;

    /**
     * @param ways
     *            chemins de la carte OSM en construction
     * 
     * @param relations
     *            relations de la carte OSM en construction
     */
    public OSMMap(Collection<OSMWay> ways, Collection<OSMRelation> relations) {
        this.ways = Collections.unmodifiableList(new ArrayList<>(ways));
        this.relations = Collections.unmodifiableList(new ArrayList<>(relations));
    }

    /**
     * @return la liste des chemins de la carte
     */
    public List<OSMWay> ways() {
        return ways;
    }

    /**
     * @return retourne la liste des relations de la carte
     */
    public List<OSMRelation> relations() {
        return relations;
    }

    /**
     * Le bàtisseur d'OSMMap.
     */
    public final static class Builder {										
        private final Map<Long, OSMWay> ways = new HashMap<>();
        private final Map<Long, OSMRelation> relations = new HashMap<>();
        private final Map<Long, OSMNode> nodes = new HashMap<>();

        /**
         * @param newNode
         *            le nouveau noeud donne au bâtisseur
         */
        public void addNode(OSMNode newNode) {
            nodes.put(newNode.id(), newNode);
        }

        /**
         * @param id
         *            l'identifiant du noeud cherché dans le bâtisseur
         * 
         * @return le noeud correspondant à l'identifiant, ou null si
         *         le noeud n'est pas dans la liste
         */
        public OSMNode nodeForId(long id) {
            return nodes.get(id);
        }

        /**
         * @param newWay
         *            le nouveau chemin a ajouter à la carte en construction
         */
        public void addWay(OSMWay newWay) {
            ways.put(newWay.id(), newWay);
        }

        /**
         * @param id
         *            l'identifiant du chemin cherché dans le bâtisseur
         * 
         * @return le chemin correspondant â l'identifiant, ou null si
         *         ce chemin n'a pas ete ajouté
         */
        public OSMWay wayForId(long id) {
                return ways.get(id);
        }

        /**
         * @param newRelation
         *            la relation à ajouter a la carte en construction
         */
        public void addRelation(OSMRelation newRelation) {
            relations.put(newRelation.id(), newRelation);
        }

        /**
         * @param id
         *            l'identifiant de la relation cherchée dans le
         *            bâtisseur
         * 
         * @return  la relation correspondant à l'identifiant, ou null
         *         si cette relation n'a pas ete ajoute
         */
        public OSMRelation relationForId(long id) {
            return relations.get(id);
        }

        /**
         * @return la carte OSM bâtie
         */
        public OSMMap build() {
            return new OSMMap(ways.values(), relations.values());
        }

    }
}
