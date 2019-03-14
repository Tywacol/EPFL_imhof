package ch.epfl.imhof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 *         Représente une carte projetée, composée d'entités géométriques
 *         attribuées.
 *         
 * @author Pierre Gabioud (247216)
 * @author Corto Callerisa (251769)
 *
 */
public final class Map {
	private final List<Attributed<PolyLine>> polyLines;
	private final List<Attributed<Polygon>> polygons;

	/**
	 * @param polyLines
	 *            la liste d'attributed<PolyLines> attribuées à la map
	 * 
	 * @param polygons
	 *            la liste d'attributed<Polygon> attribuées à la map
	 */
	public Map(List<Attributed<PolyLine>> polyLines,
			List<Attributed<Polygon>> polygons) {
		this.polyLines = Collections
				.unmodifiableList(new ArrayList<>(polyLines));
		this.polygons = Collections.unmodifiableList(new ArrayList<>(polygons));
	}

	/**
	 * @return la liste des polylignes attribuées de la carte
	 */
	public List<Attributed<PolyLine>> polyLines() {
		return polyLines;
	}

	/**
	 * @return la liste des polygones attribués de la carte
	 */
	public List<Attributed<Polygon>> polygons() {
		return polygons;
	}

	/**
	 * Le bâtisseur imbriqué statiquement de la classe Map.
	 */
	public static class Builder {
		private List<Attributed<PolyLine>> polyLines = new ArrayList<>();
		private List<Attributed<Polygon>> polygons = new ArrayList<>();

		/**
		 * @param newPolyLine
		 *            PolyLigne attribuée à ajouter à la carte en cours de
		 *            construction
		 */
		public void addPolyLine(Attributed<PolyLine> newPolyLine) {
			polyLines.add(newPolyLine);
		}

		/**
		 * @param newPolygon
		 *            Polygon attribuée à ajouter à la carte en cours de
         *            construction
		 */
		public void addPolygon(Attributed<Polygon> newPolygon) {
			polygons.add(newPolygon);
		}

		/**
		 * @return la carte construite avec les polylignes et
		 *         polygones ajoutés		          
		 */
		public Map build() {
			return new Map(polyLines, polygons);
		}
	}
}
