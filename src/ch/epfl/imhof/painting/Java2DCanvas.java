package ch.epfl.imhof.painting;

import java.util.List;
import java.util.function.Function;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;


/**
 * @author Pierre Gabioud (247216)
 * @author Corto Callerisa (251769)
 *
 * Mise en oeuvre concrète de la toile, dessine les primitives qu'on lui
 * demande de dessiner dans une image discrète.
 */
public final class Java2DCanvas implements Canvas {
    private final Function<Point, Point> coordChange;
    private final BufferedImage image;
    private final Graphics2D ctx;


    /**
     * @param bl
     *            le point en bas à gauche de la toile
     * @param tr
     *            le point en haut à droite
     * @param width
     *            la largeur de la toile en pixels
     * @param height
     *            la hauteur de la toile en pixels
     * @param resolution
     *            la résolution de la toile en dpi
     * @param c
     *            la couleur de fond de la toile
     */
    public Java2DCanvas(Point bl, Point tr, int width, int height,
            int resolution, Color c) {
        if (width <= 0 || height <= 0 || resolution <= 0) {
            throw new IllegalArgumentException(
                    "width, height or resolution not strictly postive!");
        }
        coordChange = Point.alignedCoordinateChange(bl, new Point(0, height
                / (resolution / 72d)), tr, new Point(
                        width / (resolution / 72d), 0));
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        ctx = image.createGraphics();
        ctx.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        ctx.setColor(c.toAWTColor());
        ctx.scale(resolution / 72d, resolution / 72d);
        ctx.fillRect(0, 0, width, height);

    }

    // redéfinition de la méthode drawPolyLine de l'interface Canvas:
    // permet de dessiner sur la toile une polyligne donnée avec un style de
    // ligne donné
    @Override
    public void drawPolyLine(PolyLine polyLine, LineStyle style) {
        Path2D.Double way = wayShape(polyLine);
        
        ctx.setColor(style.getColor().toAWTColor());
        ctx.setStroke(new BasicStroke(style.getWidth(), style.getLineCap().getVal()
                , style.getLineJoin().getVal(), 10.0f, style
                .getDashingPattern(), 0f));
        ctx.draw(way);
    }

    // redéfinition de la méthode drawPolygon de l'interface Canvas:
    // permet de dessiner sur la toile un polygone donné avec une couleur donnée
    @Override
    public void drawPolygon(Polygon polygon, Color color) {
        Path2D.Double shellShape = wayShape(polygon.shell());
        Area area = new Area(shellShape);
        
        for (ClosedPolyLine closed : polygon.holes())
            area.subtract(new Area(wayShape(closed)));
        ctx.setColor(color.toAWTColor());
        ctx.fill(area);
    }

    /**
     * @return l'image de la toile
     */
    public BufferedImage image() {
        return image;
    }

    // crée une forme Path2D.Double à partir de la polyline en argument
    private Path2D.Double wayShape(PolyLine polyLine) {
        List<Point> points = polyLine.points();
        Path2D.Double way = new Path2D.Double();
        
        Point projected = coordChange.apply(polyLine.firstPoint());
        way.moveTo(projected.x(), projected.y());
        
        for (int i = 1; i < points.size(); i++) {  
            projected = coordChange.apply(points.get(i));
            way.lineTo(projected.x(), projected.y());
        }

        if (polyLine.isClosed())
            way.closePath();
        return way;
    }
}
