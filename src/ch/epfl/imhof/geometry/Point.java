package ch.epfl.imhof.geometry;

import java.util.function.Function;

/**
 * Un point dans le plan en coordonnées cartesiennes.
 * 
 * @author Pierre Gabioud (247216)
 * @author Corto Callerisa (251769)
 *
 */

public final class Point  {
	private final double x, y;

	/**
	 * @param x
	 *            la coordonnée x du point en coordonnées cartésiennes
	 * 
	 * @param y
	 *            la coordonnée y du point en coordonnées cartésiennes
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return la coordonnée x du point en coordonnées cartésiennes
	 */
	public double x() {
		return x;
	}

	/**
	 * @return la coordonnée y du point en coordonnées cartésiennes
	 */
	public double y() {
		return y;
	}
	
	
	/**
	 * Méthode déterminant et éffectuant le changement de repère.
	 * 
	 * @param p1 le premier point.
	 * @param p2 sa projection dans l'autre repère aligné.
	 * @param q1 le deuxième  point.
	 * @param q2 sa projection dans l'autre repère aligné.
	 * @return la fonction caractérisant le changement de repère.
	 */
	public static Function<Point, Point> alignedCoordinateChange(Point p1, Point p2,Point q1, Point q2) {
	    if (p1.x() == q1.x() || p1.y() == q1.y() || p2.x() == q2.x() || p2.y() == q2.y() )
	        throw new IllegalArgumentException();
	    
	    double aX = (p2.x() - q2.x())/(p1.x() - q1.x());
	    double bX = (q2.x() - (aX*q1.x()));
	    double aY = (p2.y() - q2.y())/(p1.y() - q1.y());
	    double bY = (q2.y() - (aY*q1.y()));
	    
	    return p -> new Point(aX*p.x() + bX, aY*p.y() + bY);         
	}    	
}

