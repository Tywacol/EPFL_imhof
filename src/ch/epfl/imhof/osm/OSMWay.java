package ch.epfl.imhof.osm;										

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.Attributes;

/**
 * @author Pierre Gabioud (247216)
 * @author Corto Callerisa (251769)
 * 
 * Représente un chemin OpenStreeMap.
 */
public final class OSMWay extends OSMEntity {
	private final List<OSMNode> nodes;

	/**
	 * @param id
	 *            identifiant du chemin construit
	 * 
	 * @param nodes
	 *            liste de noeuds du chemin construit
	 * 
	 * @param attributes
	 *            attributs du chemin construit
	 * 
	 * @throws IllegalArgumentException
	 *             si la liste de noeuds possède moins de deux élements
	 */
	public OSMWay(long id, List<OSMNode> nodes, Attributes attributes)
			throws IllegalArgumentException {
		super(id, attributes);
		this.nodes = Collections.unmodifiableList(new ArrayList<>(nodes));
		if (nodes.size() < 2) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * @return le nombre de noeuds du chemin
	 */
	public int nodesCount() {
		return nodes.size();
	}

	/**
	 * @return la liste des noeuds du chemin
	 */
	public List<OSMNode> nodes() {
		return nodes;
	}

	/**
	 * @return la liste des noeuds du chemin, sans le dernier si celui-ci est
	 *         identique au premier
	 */
	public List<OSMNode> nonRepeatingNodes() {

		if (isClosed()) {
		    List<OSMNode> cutedNode = new ArrayList<>();
			for (OSMNode node : nodes.subList(nodes.indexOf(firstNode()), nodes.size() - 1)) 
				cutedNode.add(node);
			return cutedNode;
			
		} else
			return nodes;
	}

	/**
	 * @return le premier noeud du chemin
	 */
	public OSMNode firstNode() {
		return nodes.get(0);
	}

	/**
	 * @return le dernier noeud du chemin
	 */
	public OSMNode lastNode() {
		int fin = nodes.size() - 1;
		return nodes.get(fin);
	}

	/**
	 * @return VRAI si le chemin est fermé, FAUX sinon
	 */
	public boolean isClosed() {
		return firstNode().equals(lastNode());
	}

	/**
	 * Le bâtisseur de la classe OSMWay.
	 */
	public final static class Builder extends OSMEntity.Builder {
		private final List<OSMNode> nodes = new ArrayList<>();

		/**
		 * @param id
		 *            identifiant du chemin
		 */
		public Builder(long id) {
			super(id);
		}

	    /**
         * Méthode ajoutant un noeud à la fin de la liste de noeuds du chemin en cours de
         * construction
         * 
         * @param newNode
         *            le noeud ajouté à la liste
         * 
         */
		public void addNode(OSMNode newNode) {
			nodes.add(newNode);
		}

		/**
		 * @return construit un chemin ayant les noeuds et les attributs ajoutés
		 *         jusqu'à présent
		 * 
		 * @throws IllegalStateException
		 *             si le chemin en cours de construction est incomplet
		 */
		public OSMWay build() throws IllegalStateException {
			if (!this.isIncomplete()) {
				Attributes attributes = super.attributes.build();
				return new OSMWay(super.id, nodes, attributes);
			} else {
				throw new IllegalStateException();
			}
		}

		/**
		 * Redéfinie isIncomplete afin qu'un chemin en cours de construction
		 * mais possédant moins de deux noeuds soit également considéré comme
		 * incomplet.
		 */
		@Override
		public boolean isIncomplete() {
			if (nodes.size() < 2 || super.isIncomplete()) {
				return true;
			} else {
				return false;
			}

		}

	}
}
