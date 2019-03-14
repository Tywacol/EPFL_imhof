package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Pierre Gabioud (247216)
 * @author Corto Callerisa (251769)
 *
 * Classe représentant un graphe non orienté.
 * 
 * @param <N>
 *            paramètre générique représentant le type des noeuds du graphe
 * 
 */
public final class Graph<N> {
	private final Map<N, Set<N>> neighbors;

	/**
	 * @param neighbors
	 *            la table d'adjacence à la base du graphe en construction
	 */
	public Graph(Map<N, Set<N>> neighbors) {
		Map<N, Set<N>> map = new HashMap<>();
		for (Map.Entry<N, Set<N>> n : neighbors.entrySet()) {
			Set<N> set = Collections
					.unmodifiableSet(new HashSet<>(n.getValue()));
			map.put(n.getKey(), set);
		}
		this.neighbors = Collections.unmodifiableMap(map);
	}

	/**
	 * @return l'ensemble des noeuds du graphe
	 */
	public Set<N> nodes() {
		return neighbors.keySet();
	}

	/**
	 * @param node
	 *            un noeud dont les voisins sont cherchés
	 * 
	 * @return les noeuds voisins du noeud en argument
	 * 
	 * @throws IllegalArgumentException
	 *             si le noeud donné n'appartient pas au graphe
	 */
	public Set<N> neighborsOf(N node) throws IllegalArgumentException {
		if (!neighbors.containsKey(node)) {
			throw new IllegalArgumentException();
		}
		return neighbors.get(node);
	}

	/**
	 * @param <N>
	 *            paramètre générique représentant le type des noeuds du graphe
	 */
	public static final class Builder<N> {
		private final Map<N, Set<N>> neighbors = new HashMap<>();

		/**
		 * @param n
		 *            noeud ajouté au graphe en cours de construction, si
		 *            celui-ci n'en fait pas déjà membre
		 */
		public void addNode(N n) {
			if (!neighbors.containsKey(n)) {
				neighbors.put(n, new HashSet<N>());
			}
		}

		/**
		 * @param n1
		 *            premier noeud de l'arrête
		 * 
		 * @param n2
		 *            second noeud de l'arrête
		 * 
		 * @throws IllegalArgumentException
		 *             si l'un des noeuds n'appartient pas au graphe en cours de
		 *             construction
		 */
		public void addEdge(N n1, N n2) throws IllegalArgumentException {
			if (neighbors.containsKey(n1) && neighbors.containsKey(n2)) {
				neighbors.get(n1).add(n2);
				neighbors.get(n2).add(n1);
			} else {
				throw new IllegalArgumentException();
			}
		}

		/**
		 * @return le graphe composé des noeuds et arrêtes ajoutés
		 *         lors de la construction
		 */
		public Graph<N> build() {
			return new Graph<N>(neighbors);
		}
	}
}
