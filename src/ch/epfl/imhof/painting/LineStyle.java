package ch.epfl.imhof.painting;
import java.awt.BasicStroke;

/**
 * @author Pierre Gabioud (247216)
 * @author Corto Callerisa (251769)
 *
 *         Regroupe tous les paramètres de style utiles au dessin d'une ligne
 */
public final class LineStyle {
    private final float width;
    private final Color color;

    public static enum LineCap {
        Butt(BasicStroke.CAP_BUTT), Round(BasicStroke.CAP_ROUND), Square(BasicStroke.CAP_SQUARE);
        int linecap;
        
        private LineCap(int linecap) {
            this.linecap = linecap;
        }
        public int getVal() {
            return linecap;
        }
        
        
    };

    public static enum LineJoin {
        Bevel(BasicStroke.JOIN_BEVEL), Miter(BasicStroke.JOIN_MITER), Round(BasicStroke.JOIN_ROUND);
        
        int lineJoin;
        
        private LineJoin(int lineJoin) {
            this.lineJoin = lineJoin;
        }
        public int getVal() {
            return lineJoin;
        }
        
    };

    private final float[] dashing_pattern;
    private final LineCap lineCap;
    private final LineJoin lineJoin;

    /**
     * @param width
     *            largeur de la ligne
     * @param color
     *            couleur de la ligne
     * @param lineCap
     *            terminaison de la ligne
     * @param lineJoin
     *            jointure des segments
     * @param dashing_pattern
     *            tableau d'entier symbolisant l'alternance des sections opaques
     *            et transparentes de la ligne
     */
    public LineStyle(float width, Color color, LineCap lineCap,
            LineJoin lineJoin, float... dashing_pattern) {
   
        if (width < 0)
            throw new IllegalArgumentException("Negative width !");
        
        if (dashing_pattern == null) {
            this.dashing_pattern = null;
        } else {
            for (float f : dashing_pattern) {
                if (f <= 0)
                    throw new IllegalArgumentException();
            }
            this.dashing_pattern = dashing_pattern.clone();
        }

        this.width = width;
        this.color = color;
        this.lineCap = lineCap;
        this.lineJoin = lineJoin;
    }

    /**
     * @param width
     *            la largeur de la ligne
     * @param color
     *            la couleur de la ligne
     */
    public LineStyle(float width, Color color) {
        this(width, color, LineCap.Butt, LineJoin.Miter, null);        
    }

    /**
     * @return la largeur de la ligne
     */
    public float getWidth() {
        return width;
    }

    /**
     * @return la couleur de la ligne
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return la terminaison de la ligne
     */
    public LineCap getLineCap() {
        return lineCap;
    }

    /**
     * @return la jointure de segments
     */
    public LineJoin getLineJoin() {
        return lineJoin;
    }

    /**
     * @return le tableau d'entier symbolisant l'alternance des sections opaques
     *         et transparentes de la ligne
     */
    public float[] getDashingPattern() {
        return dashing_pattern;
    }

    /**
     * @param width
     *            la largeur de la ligne
     * 
     * @return retourne un style identique à celui auquel on l'applique, mais
     *         avec la largeur passée en argument.
     */
    public LineStyle withWidth(float width) {
        return new LineStyle(width, color, lineCap, lineJoin, dashing_pattern);
    }

    /**
     * @param color
     *            la couleur de la ligne
     * 
     * @return retourne un style identique à celui auquel on l'applique, mais
     *         avec la couleur passée en argument.
     */
    public LineStyle withColor(Color color) {
        return new LineStyle(width, color, lineCap, lineJoin, dashing_pattern);
    }

    /**
     * @param lineCap
     *            la terminaison de la ligne
     * 
     * @return retourne un style identique à celui auquel on l'applique, mais
     *         avec la terminaison passée en argument.
     */
    public LineStyle withLineCap(LineCap lineCap) {
        return new LineStyle(width, color, lineCap, lineJoin, dashing_pattern);
    }

    /**
     * @param lineJoin
     *            la jointure des segments
     * 
     * @return retourne un style identique à celui auquel on l'applique, mais
     *         avec la jointure passée en argument.
     */
    public LineStyle withLineJoin(LineJoin lineJoin) {
        return new LineStyle(width, color, lineCap, lineJoin, dashing_pattern);
    }

    /**
     * @param dashing_pattern
     *            le tableau d'entier symbolisant l'alternance des sections
     *            opaques et transparentes de la ligne
     * 
     */
    public LineStyle withDashing_Pattern(float... dashing_pattern) {
        return new LineStyle(width, color, lineCap, lineJoin, dashing_pattern);
    }

}
