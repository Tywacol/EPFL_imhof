package ch.epfl.imhof;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.dem.HGTDigitalElevationModel;
import ch.epfl.imhof.dem.ReliefShader;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.painting.Painter;
import ch.epfl.imhof.painting.SwissPainter;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;

/**
 * @author Pierre Gabioud - Sciper 247216
 * @author Corto Callerisa - Sciper 251769
 *
 *         Programme principal du programme (classe Main).
 */
public final class Main {
    // La projection de la carte.
    private final static Projection PROJECTION = new CH1903Projection();
    // Vecteur réprésentant la direction de la lumière
    private final static Vector3 LIGHT = new Vector3(-1, 1, 1);
    // Rayon de floutage, en m.
    private final static double BLURRED_RAY = 0.0017;
    // Echelle de la carte
    private final static double SCALE = 0.000_04;

    /**
     * @param args
     *            les arguments sous la forme d'un tableau de chaine de
     *            caractères
     * 
     * @throws IOException
     *             si un erreur survient lors de la lecture/écriture d'un
     *             fichier.
     * @throws org.xml.sax.SAXException
     *             si un erreur survient lors du "parsing" du fichier OSM
     *             (format XML).
     */
    public final static void main(String[] args) throws IOException,
            org.xml.sax.SAXException {

        // Lecture et assignation des points bas-gauche et haut-droit le la
        // carte, donnés en degrés.
        double botLong = Math.toRadians((Double.parseDouble(args[2])));
        double botLat = Math.toRadians((Double.parseDouble(args[3])));
        double topLong = Math.toRadians((Double.parseDouble(args[4])));
        double topLat = Math.toRadians((Double.parseDouble(args[5])));
        PointGeo botLeft = new PointGeo(botLong, botLat);
        PointGeo topRight = new PointGeo(topLong, topLat);
        // La projection des points bas-gauche et haut-droit le la carte.
        Point projBot = PROJECTION.project(botLeft);
        Point projTop = PROJECTION.project(topRight);

        // Lecture de la résolution de l'image à dessiner, en points par pouce
        // (dpi).
        int rDpi = (int) Double.parseDouble(args[6]);
        // La résolution de l'image, convertie en pixels par mètres.
        double r = (Double.parseDouble(args[6]) / 0.0254);
        // Le rayon de floutage
        double radius = BLURRED_RAY * r;

        // Détermination de la hauteur et de la largeur, à l'échelle 1/25000.
        int h = (int) Math.round(r * SCALE
                * (topRight.latitude() - botLeft.latitude()) * Earth.RADIUS);
        int w = (int) Math.round(((projTop.x() - projBot.x())
                / (projTop.y() - projBot.y()) * h));
        // Création du relief.
        File hgtFile = new File(args[1]);
        HGTDigitalElevationModel dem = new HGTDigitalElevationModel(hgtFile);
        ReliefShader shader = new ReliefShader(new CH1903Projection(), dem,
                LIGHT);
        BufferedImage relief = shader.shadedRelief(projBot, projTop, w, h,
                radius);
        // Le peintre de la carte.
        Painter swissPainter = SwissPainter.painter();
        // Lecture et adaptation du fichier OSM compressé avec gzip contenant
        // les données de la carte.
        OSMToGeoTransformer trans = new OSMToGeoTransformer(
                new CH1903Projection());
        OSMMap osmMap = OSMMapReader.readOSMFile(args[0], true);
        Map map = trans.transform(osmMap);
        // La toile de la carte.
        Java2DCanvas canvas = new Java2DCanvas(projBot, projTop, w, h, rDpi,
                Color.WHITE);
        // Dessin et récupération de la carte en 2 dimensions.
        swissPainter.drawMap(map, canvas);
        BufferedImage paintedMap = canvas.image();
        // Combinaison de carte et du relief par multiplications des composantes
        // deux à deux.

        // Corto 2019 : carte sans le relief
        ImageIO.write(paintedMap, "png", new File("paintedMap.png"));
        
        createImage(args, dem, relief, canvas, paintedMap);
    }

    private static void createImage(String[] args,
            HGTDigitalElevationModel dem, BufferedImage relief,
            Java2DCanvas canvas, BufferedImage paintedMap) throws IOException {
        for (int y = 0; y < relief.getHeight(); y++) {
            for (int x = 0; x < relief.getWidth(); x++) {
                Color mapColor = Color.rgb(paintedMap.getRGB(x, y));
                Color reliefColor = Color.rgb(relief.getRGB(x, y));
                Color mixedColor = mapColor.multiplyWith(reliefColor);
                paintedMap.setRGB(x, y, mixedColor.toAWTColor().getRGB());
            }
        }
        // Corto 2019 : debug flou non applique sur relief
        ImageIO.write(relief, "png", new File("relief.png"));
        // Ecrit le résultat au format PNG dans le fichier donnée en argument.
        ImageIO.write(canvas.image(), "png", new File(args[7]));
        // Fermeture du fichier HGT contenant le modèle numérique de terrain.
        dem.close();
    }
}
