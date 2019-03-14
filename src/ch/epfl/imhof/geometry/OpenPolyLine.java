package ch.epfl.imhof.geometry;

import java.util.List;

/**
 * Une polyline ouverte.
 * 
 * @author Pierre Gabioud (247216)
 * @author Corto Callerisa (251769)
 *
 */
public final class OpenPolyLine extends PolyLine {

    /**
     * Construit une polyline ouverte avec le constructeur de Polyline à partir
     * d'une liste de points.
     * 
     * @param points
     *            une liste de points
     */
    public OpenPolyLine(List<Point> points) {
        super(points);
    }

    @Override
    public boolean isClosed() { 
        return false;
    }

}
