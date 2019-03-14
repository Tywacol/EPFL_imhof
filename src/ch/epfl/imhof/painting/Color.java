package ch.epfl.imhof.painting;

/**
 *
 ** Une couleur représentée par ses composantes rouges, vertes et bleues.
 *
 * @author Pierre Gabioud (247216)
 * @author Corto Callerisa (251769)
 *
 */
public final class Color {
    private final double r, g, b;

    /**
     * La couleur « rouge » (pur).
     */
    public final static Color RED = new Color(1, 0, 0);

    /**
     * La couleur « vert » (pur).
     */
    public final static Color GREEN = new Color(0, 1, 0);

    /**
     * La couleur « bleu » (pur).
     */
    public final static Color BLUE = new Color(0, 0, 1);

    /**
     * La couleur « noir ».
     */
    public final static Color BLACK = new Color(0, 0, 0);

    /**
     * La couleur « blanc ».
     */
    public final static Color WHITE = new Color(1, 1, 1);

    /**
     * @param g
     *            l'intensité du gris dans l'intervalle [0;1].
     * @return le gris créé
     */
    public static Color gray(double g) {
        if (!(0.0 <= g && g <= 1.0))
            throw new IllegalArgumentException("invalid grey: " + g);
        return new Color(g, g, g);
    }

    // throws IllegalArgumentException
    // si l'une des composantes est hors de l'intervalle [0;1].
    private Color(double r, double g, double b) {
        if (!(0.0 <= r && r <= 1.0))
            throw new IllegalArgumentException("invalid red component: " + r);
        if (!(0.0 <= g && g <= 1.0))
            throw new IllegalArgumentException("invalid green component: " + g);
        if (!(0.0 <= b && b <= 1.0))
            throw new IllegalArgumentException("invalid blue component: " + b);

        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * Construit une couleur avec les composantes rouges, vertes et bleues
     * données, qui doivent être dans l'intervalle [0;1].
     *
     * @param r
     *            la composante rouge.
     * @param g
     *            la composante verte.
     * @param b
     *            la composante bleue.
     */
    public static Color rgb(double r, double g, double b) {
        return new Color(r, g, b);
    }
    
    /**
     * Dépaquete un entier 24 bit et construit une couleur avec les composantes
     * rouges, vertes et bleues données, qui doivent être dans l'intervalle
     * [0;1].
     * 
     * @param rgb entier 24 bit comportant les composants de la couleur
     * @return la couleur correspondant à l'entier dépaqueté
     */
    public static Color rgb(int rgb) {
        double r = ((rgb >> 16) & 0xFF)/255d;
        double g = ((rgb >> 8) & 0xFF)/255d;
        double b = (rgb & 0xFF)/255d;
        return new Color(r, g, b);
    }

    /**
     * Retourne la composante rouge de la couleur, comprise entre 0 et 1.
     *
     * @return la composante rouge de la couleur.
     */
    public double r() {
        return r;
    }

    /**
     * Retourne la composante verte de la couleur, comprise entre 0 et 1.
     *
     * @return la composante verte de la couleur.
     */
    public double g() {
        return g;
    }

    /**
     * Retourne la composante bleue de la couleur, comprise entre 0 et 1.
     *
     * @return la composante bleue de la couleur.
     */
    public double b() {
        return b;
    }

    /**
     * multiplie deux couleurs entre elles, par multiplication des composantes individuelles.
     * 
     * @param that la couleur externe multipliée
     * @return la couleur issue du produit de la couleur actuelle et de celle passée en argument
     */
    public Color multiplyWith(Color that) {
        return new Color(r * that.r, g * that.g, b * that.b);
    }

    /**
     * Convertit la couleur en une couleur AWT.
     * 
     * @return La couleur AWT correspondant à la couleur réceptrice.
     */

    /**
     * @return une couleur AWT à partir d'une couleur de la classe Color
     */
    public java.awt.Color toAWTColor() {

        return new java.awt.Color((float) r, (float) g, (float) b);

    }

}