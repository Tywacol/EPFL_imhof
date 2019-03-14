package ch.epfl.imhof.painting;

import java.util.List;
import java.util.function.Predicate;

import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

/**
 * @author Pierre Gabioud (247216)
 * @author Corto Callerisa (251769)
 *
 * Repésente un peintre
 */
public interface Painter {
    
    /**
     * @param map la carte à dessiner
     * @param canvas la toile sur laquelle on va dessiner la carte
     * 
     * Dessine une carte sur une toile 
     */
    public abstract void drawMap(Map map, Canvas canvas);
        
    /**
     * @param c la couleur unique utilisée par le peintre
     * 
     * @return un peintre dessinant l'intérieur de tous les polygones de la carte qu'il reçoit avec cette couleur.
     */
    public static Painter polygon(Color c) { 
        return (x,y) -> {
            List<Attributed<Polygon>> polygons = x.polygons();
            for (Attributed<Polygon> p : polygons) 
               y.drawPolygon(p.value(), c);         
        };
    }
    
    /**
     * @param lineStyle style de ligne définie
     * 
     * @return retourne un peintre dessinant toutes les lignes de la carte avec le style donné.
     */
    public static Painter line(LineStyle lineStyle) {
        return (x , y) -> {
            for (Attributed<PolyLine> l : x.polyLines()) 
                y.drawPolyLine(l.value(), lineStyle);   
        };     
    }
    
    /**
     * @param width la largeur de la ligne
     * @param color la couleur de la ligne
     * @param lineCap la terminaison de la ligne
     * @param lineJoin la jointure des segments
     * @param dashing_pattern tableau d'entier symbolisant l'alternance des sections opaques et transparentes de la ligne
     * 
     * @return un peintre dessinant toutes les lignes de la carte avec le style correspondant.
     */
    public static Painter line(float width,Color color, LineCap lineCap,
            LineJoin lineJoin, float... dashing_pattern) { 
       return (x, y) -> {
           LineStyle lineStyle = new LineStyle(width, color, lineCap, lineJoin, dashing_pattern);
           for (Attributed<PolyLine> l : x.polyLines()) 
               y.drawPolyLine(l.value(), lineStyle);   
       };
    }
    
    /**
     * @param width la largeur de la ligne                                                  
     * @param c la couleur de la ligne
     * 
     * @return un peintre dessinant toutes les lignes de la carte avec une largeur et une couleur définies,
     *         le reste des paramètres étant ceux par défaut.
     */
    public static Painter line(float width, Color c) { 
        return (x, y) -> {
            LineStyle lineStyle = new LineStyle(width, c);
            for (Attributed<PolyLine> l : x.polyLines()) 
                y.drawPolyLine(l.value(), lineStyle);   
        };
    }
    /**
     * @param width la largeur de la ligne
     * @param color la couleur de la ligne
     * @param lineCap la terminaison de la ligne
     * @param lineJoin la jointure des segments
     * @param dashing_pattern tableau d'entier symbolisant l'alternance des sections opaques et transparentes de la ligne
     * 
     * @return un peintre dessinant les pourtours de l'enveloppe et des trous de tous les polygones de la carte
     *         avec le style correspondant.
     */
    public static Painter outline(float width, Color color, LineCap lineCap,
            LineJoin lineJoin, float... dashing_pattern) { 
        return (x,y) -> {
            LineStyle lineStyle = new LineStyle(width, color, lineCap, lineJoin, dashing_pattern);
            for (Attributed<Polygon> p : x.polygons()) {
                y.drawPolyLine(p.value().shell(), lineStyle);
                for (ClosedPolyLine c : p.value().holes())
                    y.drawPolyLine(c, lineStyle);
            }
        };
    }
    
    /**
     * @param width la largeur de la ligne                                                  
     * @param c la couleur de la ligne
     * 
     * @return un peintre dessinant les pourtours de l'enveloppe et des trous de tous les polygones de la carte
     *         avec une largeur et une couleur d�finies, le reste des param�tres �tant ceux par d�faut.
     */
    public static Painter outline(float width, Color color) {
        return (x,y) -> {
            List<Attributed<Polygon>> polygons = x.polygons();
            LineStyle lineStyle = new LineStyle(width, color);
            for (Attributed<Polygon> p : polygons) {
                y.drawPolyLine(p.value().shell(), lineStyle); 
                for (ClosedPolyLine c : p.value().holes())
                    y.drawPolyLine(c, lineStyle);            
            }
        };
    }
    
    /**
     * @param predicate le prédicat resp�ct� par le peintre
     * 
     * @return un peintre ne considérant que les éléments de la carte satisfaisant le prédicat.
     */
    public default Painter when(Predicate<Attributed<?>> predicate) {       
        return (x, y) -> {
           Map.Builder mapB = new Map.Builder();
           for (Attributed<Polygon> pG : x.polygons()) {
               if (predicate.test(pG)) 
                   mapB.addPolygon(pG);        
           }
           for (Attributed<PolyLine> pL : x.polyLines()) {
               if (predicate.test(pL)) 
                   mapB.addPolyLine(pL);
           }
           drawMap(mapB.build(), y);
        };
    }
    
    /**
     * @param painter un peintre
     * 
     * @return un peintre dessinant d'abord la carte produite par le peintre pris en argument puis, 
     *         par dessus, la carte produite par le premier peintre.
     */
    public default Painter above(Painter painter) {      
        return (x, y) -> {
            painter.drawMap(x, y);  
            drawMap(x, y);   
         
        };
    }
    
    
    /**
     * @return un peintre utilisant l'attribut layer attach� aux entit�s de la carte pour la dessiner par couches
     */
    public default Painter layered() { 
        return (x, y) -> {
            Painter paint = when(Filters.onLayer(-5));
            for (int i = -4; i<=5; i++) {                
               paint = when(Filters.onLayer(i)).above(paint);
            }
            paint.drawMap(x, y); 
        };
    }
    
    
    
    
}
