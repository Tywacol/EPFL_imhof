package ch.epfl.imhof;

/**
 * @author Pierre Gabioud (247216)
 * @author Corto Callerisa (251769)
 *
 * Représente une entite de type T dotée d'attributs.
 *
 * @param <T>
 *            paramètre générique de la classe
 * 
 */
public final class Attributed<T> {
	private final T value;
	private final Attributes attributes;

	/**
	 * @param value
	 *            valeur à laquelle sont attachés les attributs
	 * 
	 * @param attributes
	 *            attributs correspondants à la valeur
	 */
	public Attributed(T value, Attributes attributes) {
		this.value = value;
		this.attributes = attributes;
	}

	/**
	 * @return la valeur
	 */
	public T value() {
		return value;
	}

	/**
	 * @return les attributs
	 */
	public Attributes attributes() {
		return attributes;
	}

	/**
	 * @param attributeName
	 *            nom de l'attribut, dont on vérifie l'appartenance à l'ensemble
	 *            d'attributs
	 * @return VRAI si et seulememt si les attributs incluent celui dont le nom
	 *         est passé en argument, FAUX sinon
	 */
	public boolean hasAttribute(String attributeName) {
		return attributes.contains(attributeName);
	}

	/**
	 * @param attributeName
	 *            nom de l'attribut dont la valeur est demandée
	 *            
	 * @return la valeur associée à attributeName, null par défaut
	 */
	public String attributeValue(String attributeName) {
		return attributes.get(attributeName);
	}

	/**
	 * @param attributeName
	 *            nom de l'attribut dont la valeur est demandée
	 * @param defaultValue
	 *            valeur retourée par défaut de type String
	 *            
	 * @return la valeur associée à attributeName ou la valeur par défaut
	 */
	public String attributeValue(String attributeName, String defaultValue) {
		return attributes.get(attributeName, defaultValue);
	}

	/**
	 * @param attributeName
	 *            nom de l'attribut dont la valeur est demandée
	 * @param defaultValue
	 *            valeur retournée par défaut de type int
	 *            
	 * @return la valeur associée à attributeName, la valeur par défaut si celle ci n'est pas définie
	 */
	public int attributeValue(String attributeName, int defaultValue) {
		return attributes.get(attributeName, defaultValue);
	}
}
