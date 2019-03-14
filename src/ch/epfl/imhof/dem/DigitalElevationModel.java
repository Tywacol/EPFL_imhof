package ch.epfl.imhof.dem;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

/**
 * @author Pierre Gabioud - Sciper 247216
 * @author Corto Callerisa - Sciper 251769
 *
 *         Représente un modéle numérique du terrain.
 */
public interface DigitalElevationModel extends AutoCloseable {

    /**
     * @param point
     *            le point dont on va calculer le vecteur normal
     * 
     * @return le vecteur normal à la Terre en ce point
     * 
     * @throws IllegalArgumentException
     *             si le point pour lequel la normale est demandé ne fait pas
     *             partie de la zone couverte par le MNT
     */
    public Vector3 normalAt(PointGeo point) throws IllegalArgumentException;
}
