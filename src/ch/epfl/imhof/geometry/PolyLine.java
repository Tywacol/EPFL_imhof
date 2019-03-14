package ch.epfl.imhof.geometry;					//MODIF

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Représente une polyline en général.
 * 
 * @author Pierre Gabioud (247216)
 * @author Corto Callerisa (251769)
 *
 */
public abstract class PolyLine {

	/**
	 * La liste de points formant la polyline.
	 */
	private final List<Point> points;

	/**
	 * @param points
	 *            La liste des points formant la polyline.
	 * @throws IllegalArgumentException
	 *             si la liste passée en argument est vide ou nulle
	 * 
	 */
	public PolyLine(List<Point> points) throws IllegalArgumentException {
		if (points.isEmpty() || points == null) {
			throw new IllegalArgumentException();
		}
		this.points = Collections.unmodifiableList(new ArrayList<>(points));
	}

	public abstract boolean isClosed();

	/**
	 * @return la liste de points immuable.
	 */
	public List<Point> points() {
		return points;										
	}

	/**
	 * @return le premier point de la liste points
	 */
	public Point firstPoint() {
		return points.get(0);
	}

	/**
	 * 
	 * Le batisseur imbriqué statiquement de la classe polyline.
	 *
	 */
	public final static class Builder {
		private List<Point> points  = new ArrayList<Point>();

		/**
		 * Ajoute un point à la fin de la liste.
		 * 
		 * @param newPoint
		 *            le point ajouté
		 */
		public void addPoint(Point newPoint) {
			points.add(newPoint);
		}

		/**
		 * Crée la polyline ouverte avec les points ajoutés.
		 * 
		 * @return la polyline ouverte avec les points ajoutes
		 */
		public OpenPolyLine buildOpen() {
			return new OpenPolyLine(points);
		}

		/**
		 * crée la polyline fermée avec les points ajoutes
		 * 
		 * @return la polyline fermée avec les points ajoutés
		 */
		public ClosedPolyLine buildClosed() {
			return new ClosedPolyLine(points);
		}
	}
}
