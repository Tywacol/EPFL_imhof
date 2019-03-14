package ch.epfl.imhof.painting;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * Interface représentant une toile 
 * 
 * @author Pierre Gabioud (247216)
 * @author Corto Callerisa (251769)
 *
 */
public interface Canvas {
    /**
     * méthode dessinant sur une toile une PolyLine avec le style donné
     * 
     * @param polyLine la PolyLine a dessiner
     * @param lineStyle le style de dessin
     */
    public void drawPolyLine(PolyLine polyLine, LineStyle lineStyle);
    /**
     * méthode dessinant sur une toile un Polygone et le remplissant avec la couleur donnée
     * 
     * @param polygon le Polygone à dessiner
     * @param color la couleur de remplissage
     */
    public void drawPolygon(Polygon polygon, Color color);
    
}
