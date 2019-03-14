package ch.epfl.imhof.dem;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.junit.Test;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.painting.Color;

public class HGTDigitalElevationModelTest {

    @Test
    public void test() throws IOException {

        // essayer avec un try-with-ressources
        String N46E007 = getClass().getResource("N46E007.hgt").getFile();
        File file = new File(N46E007);
        HGTDigitalElevationModel dem = new HGTDigitalElevationModel(file);
        BufferedImage image = new BufferedImage(800, 800,
                BufferedImage.TYPE_INT_RGB);

        // il semble qu'il y ai une inversion
        double longBase = 7.2;
        double latBase = 46.2;
        double scale = 0.6 / 799;

        for (int y = 0; y < 800; y++) {
            for (int x = 0; x < 800; x++) {

                PointGeo pg = new PointGeo(Math.toRadians(longBase),
                        Math.toRadians(latBase));
                longBase += scale;
                Vector3 v = dem.normalAt(pg);

                Color g = Color.gray(0.5 * (v.normalized().y() + 1));
                image.setRGB(x, y, g.toAWTColor().getRGB());
            }
            latBase += scale;
            longBase = 7.2;
        }
        // ImageIO.write(image, "png", new
        // File("ReliefEtape10TESTApresCrash.png"));
        dem.close();
    }

}
