package ch.epfl.imhof.osm;								

import ch.epfl.imhof.Attributes;

/**
 * @author Pierre Gabioud (247216)
 * @author Corto Callerisa (251769)
 * 
 * Classe mère de toutes les classes représentant les entités OpenStreetMap.
 */
public abstract class OSMEntity {
	private final long id;
	private Attributes attributes;													

	/**
	 * @param id
	 *            l'identifiant unique de l'entité OSM
	 * 
	 * @param attributes
	 *            les attributs de l'entité
	 */
	public OSMEntity(long id, Attributes attributes) {
		this.id = id;
		this.attributes = attributes;
	}

	/**
	 * @return l'identifiant unique de l'entité
	 */
	public long id() {
		return id;
	}

	/**
	 * @return retourne les attributs de l'entité
	 */
	public Attributes attributes() {
		return attributes;
	}

	/**
	 * @param key
	 *            l'identifiant de l'attribut dont on teste l'appartenance à
	 *            l'entité OSM
	 * 
	 * @return retourne VRAI si et seulement si l'entité possède l'attribut
	 *         passé en argument, FAUX sinon
	 */
	public boolean hasAttribute(String key) {
		return attributes.contains(key);
	}

	/**
	 * @param key
	 *            l'identifiant de l'attribut
	 * 
	 * @return retourne l'attribut correspondant à l'identifiant, ou null si
	 *         celui-ci n'existe pas
	 */
	public String attributeValue(String key) {
		return attributes.get(key);
	}

	/**
	 * Le squelette de tout les batisseurs des classes héritant de
	 * OSMEntity.
	 */
	public abstract static class Builder {
		protected final long id;
		protected Attributes.Builder attributes = new Attributes.Builder();
		private boolean incomplete = false;

		/**
		 * @param id
		 *            entier identifiant une entité OSM
		 * 
		 *            construit un batisseur pour une entite OSM identifiée par
		 *            l'entier donné
		 */
		public Builder(long id) {
			this.id = id;
		}

        /**
         * 
         * Ajoute l'association (clef, valeur) donnée a l'ensemble d'attributs
         * de l'entité en cours de construction. Si un attribut de même nom
         * avait déjà été ajouté précédemment, sa valeur est remplacée par celle
         * donnée
         * 
         * @param key
         *            clef associée à une valeur, toutes deux données à
         *            l'ensemble d'attributs de l'entité
         * 
         * @param value
         *            valeur associée à une clef, toutes deux données à
         *            l'ensemble d'attributs de l'entité
         * 
         */
		public void setAttribute(String key, String value) {
			attributes.put(key, value);
		}

		/**
		 * Declare que l'entité en cours de construction est incomplète.
		 */
		public void setIncomplete() {
			incomplete = true;
		}
		
		/**
		 * @return  VRAI si et seulement si l'entité en cours de
		 *         construction est incomplète
		 */
		public boolean isIncomplete() {
			return incomplete;
		}
	}

}
