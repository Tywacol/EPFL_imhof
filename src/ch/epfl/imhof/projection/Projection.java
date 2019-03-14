package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/**
 * @author Pierre Gabioud (247216)
 * @author Corto Callerisa (251769)
 * 
 * Interface abstraite projetant un point géographique en coordonée
 * cartésiennes.
 */
public interface Projection {
	/**
	 * @param point
	 *            Objet de classe PointGeo à convertir
	 * 
	 * @return le point géographique projeté sous un objet de classe
	 *         Point
	 */
	public Point project(PointGeo point);

	/**
	 * Methode abstraite projetant un point cartésien en coordonnées
	 * géographiques
	 * 
	 * @param point
	 *            Objet de classe Point à convertir
	 *            
	 * @return le point cartésien projeté en un objet de classe
	 *         PointGeo
	 */
	public PointGeo inverse(Point point);
}
