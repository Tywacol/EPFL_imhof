package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/**
 * @author Pierre Gabioud (247216)
 * @author Corto Callerisa (251769)
 *
 * La classe représentant la projection rectangulaire.
 */

public final class EquirectangularProjection implements Projection {

	/**
	 * @param x
	 *            reçois la longitude du point
	 * @param y
	 *            reçois la latitude du point
	 *            
	 * @return le Point issu de la projection
	 */

	@Override
    public Point project(PointGeo point) {
		double x = point.longitude();
		double y = point.latitude();
		return new Point(x, y);
	}

	/**
	 * @param longitude
	 *            reçois la coordonée x du point
	 * @param latitude
	 *            reçois la coordonnée y du point
	 *            
	 * @return le PointGeo issu de la projection.
	 */

	@Override
    public PointGeo inverse(Point point) {
		double longitude = point.x();
		double latitude = point.y();
		return new PointGeo(longitude, latitude);
	}

}
