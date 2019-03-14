package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Représente un ensemble d'attributs ainsi que leur valeur associée.
 * 
 * @author Pierre Gabioud (247216)
 * @author Corto Callerisa (251769)
 *
 */
public final class Attributes {

	private final Map<String, String> m;

	/**
	 * @param attributes
	 *            ensemble d'attributs avec paires clefs/valeurs
	 */
	public Attributes(Map<String, String> attributes) {
		this.m = Collections.unmodifiableMap(new HashMap<>(attributes));
	}

	/**
	 * @return VRAI si et seulement si l'ensemble d'attributs est vide, FAUX sinon
	 */
	public boolean isEmpty() {
		return m.isEmpty();
	}

	/**
	 * @param key
	 *            clef de la paire clefs/valeurs
	 * 
	 * @return VRAI si l'ensemble d'attributs contient la clef donnée, FAUX sinon
	 */
	public boolean contains(String key) {
		return m.containsKey(key);
	}

	/**
	 * @param key
	 *            clef de la paire clefs/valeurs
	 * 
	 * @return la valeur associée a la clef, ou null si aucune valeur est
	 *         associée
	 */
	public String get(String key) {
		return m.get(key);
	}

	/**
	 * @param key
	 *            clef de la paire clefs/valeurs
	 * 
	 * @param defaultValue
	 *            valeur de type String retournée par défaut si la clef n'est pas dans la table
	 * 
	 * @return la valeur associée à la clef si elle existe, la valeur par défault sinon
	 * 
	 */
	public String get(String key, String defaultValue) {
		return m.getOrDefault(key, defaultValue);
	}

	/**
	 * @param key
	 *            clef de la paire clefs/valeurs
	 * 
	 * @param defaultValue
	 *            valeur de type int retournée par défaut si la clef n'est pas dans la table
	 *            
	 * @return la valeur associée à la clef si elle existe, la valeur par défault sinon
	 * 
	 */

	public int get(String key, int defaultValue) {
		int val = defaultValue;
		if (m.get(key) != null) {
			try {
				val = Integer.parseInt(m.get(key));
			} catch (NumberFormatException e) {
			 // val = defaultValue car l'Exception ne la modifie pas
				return val; 
			}
		}
		return val;
	}

	/**
	 * @param keysToKeep
	 *            l'ensemble des clefs à garder
	 *            
	 * @return une version filtrée des attributs ne contenant que ceux dont le
	 *         nom figure dans l'ensemble donné en argument
	 */
	public Attributes keepOnlyKeys(Set<String> keysToKeep) {
		Attributes.Builder mFiltr = new Builder();
		
		for (Map.Entry<String, String> e : m.entrySet()) {
			String key = e.getKey();
			
			if (keysToKeep.contains(key)) {    
				String val = e.getValue();
				mFiltr.put(key, val);
			}
		}
		return mFiltr.build();

	}

	/**
	 * Le bâtisseur de la classe Attributes.
	 *
	 */
	public final static class Builder {
		private Map<String, String> m = new HashMap<>();

		/**
		 * @param key
		 *            la clef de la paire clefs/valeurs
		 * 
		 * @param value
		 *            la valeur de la paire clefs/valeurs
		 */
		public void put(String key, String value) {
			m.put(key, value);
		}

		/**
		 * @return l'attribut construit
		 */
		public Attributes build() {
			return new Attributes(m);
		}

	}

}
