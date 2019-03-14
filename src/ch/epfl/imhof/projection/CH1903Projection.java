package ch.epfl.imhof.projection;

import java.lang.Math;
import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/**
 * @author Pierre Gabioud (247216)
 * @author Corto Callerisa (251769)
 * 
 * La classe représentant la projection Ch1903.
 * 
 */
public final class CH1903Projection implements Projection {

	/**
	 * @param lambda
	 *            la longitude, en degrés, du PointGeo projeté.
	 * @param theta
	 *            la latitude, en degrés, du PointGeo projeté.
	 * @param lambda1
	 *            variable intermédiaire du calcul
	 * @param theta1
	 *            variable intermédiaire du calcul
	 * @param x
	 *            l'abcsisse cartésienne obtenue du PointGeo projeté
	 * @param y
	 *            l'ordonnée cartésienne obtenue du PointGeo projeté
	 *            
	 * @return le point projeté selon la projectionCH1903
	 */
	@Override
    public Point project(PointGeo point) {
		double lambda = Math.toDegrees(point.longitude());
		double theta = Math.toDegrees(point.latitude());

		double lambda1 = 0.0001 * (lambda * 3600 - 26782.5);
		double theta1 = 0.0001 * (theta * 3600 - 169028.66);

		double x = 600072.37 + 211455.93 * lambda1 - 10938.51 * lambda1
				* theta1 - 0.36 * lambda1 * Math.pow(theta1, 2) - 44.54
				* Math.pow(lambda1, 3);
		double y = 200147.07 + 308807.95 * theta1 + 3745.25
				* Math.pow(lambda1, 2) + 76.63 * Math.pow(theta1, 2) - 194.56
				* Math.pow(lambda1, 2) * theta1 + 119.79 * Math.pow(theta1, 3);

		return new Point(x, y);
	}

	/**
	 * Fait l'inverse de la méthode project
	 * 
	 * @param x1
	 *            variable intermédiaire du calcul
	 * @param y1
	 *            variable intermédiaire du calcul
	 * @param lambda0
	 *            variable intermédiaire du calcul
	 * @param theta0
	 *            variable intermédiaire du calcul
	 * @param x1
	 *            variable intermédiaire du calcul
	 * @param lamda
	 *            la longitude en degrés
	 * @param theta
	 *            la longitude en degrés
	 * @param longitude
	 *            la longitude en radians
	 * @param latitude
	 *            la latitude en radians
	 *            
	 * @return le PointGeo obtenu par une "dé-projection" CH1903 d'un
	 *         point cartésien
	 */

	@Override
    public PointGeo inverse(Point point) {
		double x1 = (point.x() - 600000) / 1000000;
		double y1 = (point.y() - 200000) / 1000000;

		double lambda0 = 2.6779094 + 4.728982 * x1 + 0.791484 * x1 * y1
				+ 0.1306 * x1 * Math.pow(y1, 2) - 0.0436 * Math.pow(x1, 3);
		double theta0 = 16.9023892 + 3.238272 * y1 - 0.270978 * Math.pow(x1, 2)
				- 0.002528 * Math.pow(y1, 2) - 0.0447 * Math.pow(x1, 2) * y1
				- 0.0140 * Math.pow(y1, 3);

		double lambda = lambda0 * 100 / 36.00;
		double theta = theta0 * 100 / 36.00;

		double longitude = Math.toRadians(lambda);
		double latitude = Math.toRadians(theta);

		return new PointGeo(longitude, latitude);
	}

}
