package ch.epfl.imhof.dem;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.function.Function;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.projection.Projection;

/**
 * @author Pierre Gabioud - Sciper 247216
 * @author Corto Callerisa - Sciper 251769
 * 
 *         Représente le relief ombré coloré.
 */
public final class ReliefShader {
    private final Projection projection;
    private final HGTDigitalElevationModel dem;
    private final Vector3 light;

    /**
     * Le constructeur de la classe ReliefShader.
     * 
     * @param projection
     *            la projection à utiliser pour passer les points en coordonnées
     *            du plan
     * @param dem
     *            le modèle numérique du terrain
     * @param light
     *            le vecteur pointant dans la direction de la source lumineuse
     */
    public ReliefShader(Projection projection, HGTDigitalElevationModel dem,
            Vector3 light) {
        this.projection = projection;
        this.dem = dem;
        this.light = light;
    }

    /**
     * @param bL
     *            le point en bas à gauche du relief à dessiner (en coordonnées
     *            du plan)
     * @param tR
     *            le point en haut à droite du relief à dessiner (en coordonnées
     *            du plan)
     * @param width
     *            la largeur en pixels de l'image à dessiner
     * @param height
     *            la hauteur en pixels de l'image à dessiner
     * @param radius
     *            le rayon de floutage
     * 
     * @return le relief ombré coloré, flouté si le rayon de floutage donné est
     *         strictement positif
     * 
     * @throws IllegalArgumentException
     *             si le rayon est n�gatif
     */
    public BufferedImage shadedRelief(Point bL, Point tR, int width,
            int height, double radius) throws IllegalArgumentException {
        if (radius < 0)
            throw new IllegalArgumentException("Negative Radius");

        int n = (int) (2 * Math.ceil(radius) + 1);
        if (radius > 0) {
            float[] kernelData = createKernel(radius);
            int buffZone = (int) Math.floor(n / 2d);
            Function<Point, Point> coordChangeTamp = Point
                    .alignedCoordinateChange(new Point(buffZone, height
                            + buffZone), bL, new Point(buffZone + width,
                            buffZone), tR);

            BufferedImage extraBasicRelief = basicShaded(width + 2 * buffZone,
                    height + 2 * buffZone, coordChangeTamp);
            Kernel kerH = new Kernel(n, 1, kernelData);
            BufferedImage blurredH = blur(extraBasicRelief, kerH);
            Kernel kerV = new Kernel(1, n, kernelData);
            BufferedImage blurredHAndV = blur(blurredH, kerV);

            return blurredHAndV.getSubimage(buffZone, buffZone, width, height);

        } else {
            Function<Point, Point> coordChange = Point.alignedCoordinateChange(
                    new Point(0, height), bL, new Point(width, 0), tR);
            return basicShaded(width, height, coordChange);
        }
    }

    /**
     * @param width
     *            la largeur de l'image à dessiner
     * @param height
     *            la hauteur de l'image à dessiner
     * @param coordChange
     *            fonction permettant de passer du repère de l'image à celui du
     *            plan
     * 
     * @return un relief ombré brut, sans floutage
     */
    private BufferedImage basicShaded(int width, int height,
            Function<Point, Point> coordChange) {
        BufferedImage basicRelief = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Point point = coordChange.apply(new Point(x, y));
                PointGeo geoPoint = projection.inverse(point);
                Vector3 vector = dem.normalAt(geoPoint);

                double cosO = (vector.normalized().scalarProduct(light
                        .normalized()));
                double r = 0.5 * (cosO + 1);
                double g = 0.5 * (cosO + 1);
                double b = 0.5 * (0.7 * cosO + 1);
                Color c = Color.rgb(r, g, b);
                basicRelief.setRGB(x, y, c.toAWTColor().getRGB());

            }
        }
        return basicRelief;
    }

    /**
     * @param radius
     *            le rayon du flou gaussien
     * 
     * @return le noyau du flou gaussien
     */
    private float[] createKernel(double radius) {
        double sigma = radius / 3d;
        int n = (int) (2 * Math.ceil(radius) + 1);
        float[] data = new float[n];
        int ind = 0;
        double sum = 0;

        for (int i = (int) -Math.floor(n / 2d); i <= Math.floor(n / 2d); i++) {
            data[ind] = (float) Math.exp(-(i * i) / (2 * sigma * sigma));
            sum += data[ind++];
        }
        for (int i = 0; i < data.length; i++) {
            data[i] /= sum;
        }
        return data;
    }

    /**
     * @param imageToBlur
     *            l'image à flouter
     * @param ker
     *            noyau du flou gaussien
     * 
     * @return une image floutée à partir du flou gaussien pris en argument
     */
    private BufferedImage blur(BufferedImage imageToBlur, Kernel ker) {
        ConvolveOp op = new ConvolveOp(ker, ConvolveOp.EDGE_NO_OP, null);
        return op.filter(imageToBlur, null);
    }
}
