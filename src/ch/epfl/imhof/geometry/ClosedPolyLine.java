package ch.epfl.imhof.geometry;

import java.util.List;
import java.lang.Math;

/**
 * Une polyline fermée.
 * 
 * @author Pierre Gabioud (247216)
 * @author Corto Callerisa (251769)
 *
 */
public final class ClosedPolyLine extends PolyLine {

    /**
     * Construit une polyline fermée avec le constructeur de Polyline à partir
     * d'une liste de points.
     * 
     * @param points
     *            une liste de points
     */
    public ClosedPolyLine(List<Point> points) {
        super(points);
    }

    
    @Override
    public boolean isClosed() {
        return true;
    }

    /**
     * 
     * @return l'aire de la polyline fermée
     * 
     */
    public double area() {
        double xi;
        double yiPlus;
        double yiMinus;

        double area = 0;

        for (int i = 0; i < points().size(); i++) {

            xi = points().get(IndiceGen(i)).x();
            yiPlus = points().get(IndiceGen(i + 1)).y();
            yiMinus = points().get(IndiceGen(i - 1)).y();

            area += xi * (yiPlus - yiMinus);
        }
        return 0.5*Math.abs(area);                                          //optimisaton approuved by PGM
    }

    /**
     * @param indice
     *            l'indice a generaliser
     * @return l'indice generalise
     */
    private int IndiceGen(int index) {
        return Math.floorMod(index, points().size());
    }

    /**
     * Vérifie l'appartenance du point à la polyline
     * 
     * @param p
     *            le point testé
     * 
     * @return VRAI si le point est à l'interieur de la polyligne fermée, FAUX sinon
     */
    public boolean containsPoint(Point p) {
        int ind = 0;
        for (int i = 0; i < points().size(); i++) {
            
            Point p1 = points().get(IndiceGen(i));
            Point p2 = points().get(IndiceGen(i + 1));
            
            if (p1.y() <= p.y()) {
                
                if ((p2.y() > p.y()) && AtLeft(p, p1, p2)) {
                    ++ind;
                }
            } else {
                if (p2.y() <= p.y() && AtLeft(p, p2, p1)) {
                    --ind;
                }
            }
        }
        return ind != 0;
    }

    /**
     * Teste si un point est à gauche d'un segment grace à l'aire signée d'un
     * triangle.
     * 
     * @param p
     *            le point testé
     * @param p1
     *            un point du segment
     * @param p2
     *            l'autre point
     * @return VRAI si le point est strictement à gauche, FALSE sinon
     */
    private boolean AtLeft(Point p, Point p1, Point p2) {  
        
        double mbreLeft = (p1.x() - p.x()) * (p2.y() - p.y());
        double mbreRight = (p2.x() - p.x()) * (p1.y() - p.y());
        
        return (mbreLeft > mbreRight);
        

    }

}
