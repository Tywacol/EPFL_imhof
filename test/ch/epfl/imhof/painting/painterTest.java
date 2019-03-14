package ch.epfl.imhof.painting;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.function.Predicate;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.xml.sax.SAXException;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.projection.CH1903Projection;

public class painterTest {

    @Test
    public void test() throws SAXException, IOException {
        //assertEquals(1, 1);
        // creation du peintre des lacs

        Predicate<Attributed<?>> isLake = Filters.tagged("natural", "water");
        Painter lakesPainter = Painter.polygon(Color.BLUE).when(isLake);

        // creation du peintre de la nature

        Predicate<Attributed<?>> isNatural = Filters.tagged("natural");

        // Painter naturalPainter =
        // Painter.polygon(Color.GREEN).when(isNatural); // premier essai avec
        // une couleur moche
        String lc = getClass().getResource("/interlaken.osm.gz").getFile();
        // creation du peintre des batiments

        Predicate<Attributed<?>> isBuilding = Filters.tagged("building");
        Painter buildingsPainter = Painter.polygon(Color.BLACK)
                .when(isBuilding);

        // creation d'un peintre de lignes

        Painter linePainter = Painter.line(1f, Color.gray(0.2));

        // creation du peintre global

        Painter painter = buildingsPainter.above(lakesPainter);
        Painter swissPainter = SwissPainter.painter();
        // Lecture depuis lausanne.osm

        OSMToGeoTransformer trans = new OSMToGeoTransformer(
                new CH1903Projection());
        OSMMap osmmap = OSMMapReader.readOSMFile(lc, true);
        Map map = trans.transform(osmmap);

        // La toile

        Point blI = new Point(628590, 168210);
        Point trI = new Point(635660, 172870);

        Point blL = new Point(532510, 150590);
        Point trL = new Point(539570, 155260);

        Java2DCanvas canvas = new Java2DCanvas(blI, trI, 16000, 8600, 1500,
                Color.WHITE); // resolution inchangée

        // Dessin de la carte et stockage dans un fichier
        
        

        swissPainter.drawMap(map, canvas);
        //ImageIO.write(canvas.image(), "png", new File("interlakenHD.png"));

    }
    
    

}
