package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;

/**
 * @author Pierre Gabioud (247216)
 * @author Corto Callerisa (251769)
 * 
 * L'entité noeud OpenStreetMap.
 */
public final class OSMNode extends OSMEntity {
	private final PointGeo position;

	/**
	 * @param id
	 *            l'identifiant du noeud OSM construit
	 * 
	 * @param position
	 *            la position du noeud OSM construit
	 * 
	 * @param attributes
	 *            les attributs du noeud OSM construit
	 */
	public OSMNode(long id, PointGeo position, Attributes attributes) {
		super(id, attributes);
		this.position = position;
	}

	/**
	 * @return la position du noeud
	 */
	public PointGeo position() {
		return position;
	}

	/**
	 * bâtisseur de la classe OSMNode
	 */
	public final static class Builder extends OSMEntity.Builder {
		private final PointGeo position;

		/**
		 * @param id
		 *            l'identifiant du noeud construit
		 * 
		 * @param position
		 *            la position du noeud construit
		 */

		public Builder(long id, PointGeo position) {
			super(id);
			this.position = position;

		}

		/**
		 * @return un noeud OSM avec l'identifiant et la position pasées au
		 *         constructeur, ainsi que les éventuels attributs ajoutés
		 * 
		 * @throws IllegalStateException
		 *             si le noeud en cours de construction est incomplet
		 */
		public OSMNode build() throws IllegalStateException {
			if (isIncomplete()) {
				throw new IllegalStateException();
			}
			return new OSMNode(id, position, super.attributes.build());
		}
	}

}