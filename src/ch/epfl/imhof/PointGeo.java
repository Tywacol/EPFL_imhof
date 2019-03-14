package ch.epfl.imhof;
import java.lang.Math;

/**
 * un point à la surface de la terre, en coordonnees
 *  sphériques.
 * 
 * @author Pierre Gabioud (247216)
 * @author Corto Callerisa (251769)
 *
 */
public final class PointGeo {
	private final double longitude, latitude;

	/**
	 * Construit un point avec la latitude et la longitude données.
	 * 
	 * @param longitude
	 *            la longitude du point, en radians
	 * @param latitude
	 *            la latitude du point, en radians
	 * @throws IllegalArgumentException
	 *             si la longitude est invalide, c-à-d hors de l'intervalle [-π;
	 *             π]
	 * @throws IllegalArgumentException
	 *             si la latitude est invalide, c-à-d hors de l'intervalle
	 *             [-π/2; π/2]
	 * 
	 */
    public PointGeo(double longitude, double latitude)
            throws IllegalArgumentException {
        if (longitude < -Math.PI || longitude > Math.PI
                || latitude < -0.5 * Math.PI || latitude > 0.5 * Math.PI) {
            throw new IllegalArgumentException("Invalid longitude or latitude");
        }
		
		this.longitude = longitude;
		this.latitude = latitude;   

	}
	/**
	 * @return la longitude
	 */
	public double longitude() {
		return longitude;
	}
	/**
	 * @return la latitude
	 */
	public double latitude() {
		return latitude;
	}

}


