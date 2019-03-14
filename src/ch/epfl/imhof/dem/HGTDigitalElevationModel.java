package ch.epfl.imhof.dem;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

/**
 * @author Pierre Gabioud - Sciper 247216
 * @author Corto Callerisa - Sciper 251769
 *
 *         Représente un Modèle Numérique de Terrain stocké dans un fichier au
 *         format HGT.
 */
public final class HGTDigitalElevationModel implements DigitalElevationModel {
    private final double size;
    private final double s;
    private final double delta;
    private final double SWpointLong;
    private final double SWpointLat;
    private final int side;
    private final FileInputStream stream;
    private ShortBuffer buffer;

    /**
     * Le constructeur de la classe HGTDigitalElevationModel.
     * 
     * @param file
     *            le fichier HGT contenant le modèle numérique du terrain
     * 
     * @throws IOException
     *             si une erreur survient lors de la fermeture du flot
     *             correspondant au fichier HGT
     */
    public HGTDigitalElevationModel(File file) throws IOException {
        if (!respectConventions(file))
            throw new IllegalArgumentException("Incorrect HGT file name");

        size = file.length();
        double latFile = Integer.parseInt(file.getName().substring(1, 3));
        // Détermination de la latitude du point bas-gauche par un opérateur
        // ternaire.
        SWpointLat = file.getName().charAt(0) == 'N' ? Math.toRadians(latFile)
                : -Math.toRadians(latFile);
        double longFile = Integer.parseInt(file.getName().substring(4, 7));
        // Détermination de la longitude du point bas-gauche par un opérateur
        // ternaire.
        SWpointLong = file.getName().charAt(3) == 'E' ? Math
                .toRadians(longFile) : -Math.toRadians(longFile);
        side = (int) (Math.sqrt(size / 2d) - 1);
        delta = Math.toRadians(1d / (side));
        s = delta * Earth.RADIUS;
        stream = new FileInputStream(file);
        buffer = stream.getChannel().map(MapMode.READ_ONLY, 0, file.length())
                .asShortBuffer();
    }

    // Redéfinition de la méthode close(). Ferme la ressource et libère la
    // mémoire associée.
    @Override
    public void close() throws IOException {
        stream.close();
        buffer = null;
    }

    // Retourne le vecteur, de classe Vector3, normal à la terre du point pris
    // en argument.
    @Override
    public Vector3 normalAt(PointGeo point) throws IllegalArgumentException {
        if (!IsInDomain(point))
            throw new IllegalArgumentException(
                    "The point is not in the field of the HGT file");
        double i = Math.floor((point.longitude() - SWpointLong) / delta);
        double j = Math.floor((point.latitude() - SWpointLat) / delta);

        int alt1 = getAlt(i, j);
        int alt2 = getAlt(i, j + 1);
        int alt3 = getAlt(i + 1, j + 1);
        int alt4 = getAlt(i + 1, j);

        double x = 0.5 * s * (alt1 - alt4 + alt2 - alt3);
        double y = 0.5 * s * (alt1 + alt4 - alt2 - alt3);
        double z = s * s;
        return new Vector3(x, y, z);
    }

    /**
     * Méthode utilisée par la méthode normalAt() afin d'accéder à l'altitude
     * mappée en mémoire du point quadrlillant le point passé en argument à la
     * méthode normalAt(), selon les arguments i et j donnés.
     * 
     * @param i
     *            abscisse d'une altitude quadrillant le point passé en argument
     *            à la méthode normalAt(), dans le fichier HGT vu sous la forme
     *            d'un tableau
     * @param j
     *            ordonnée d'une altitude quadrillant le point passé en argument
     *            à la méthode normalAt(), dans le fichier HGT vu sous la forme
     *            d'un tableau
     * 
     * @return l'altitude obtenue à partir des index issues des coordonnés du
     *         point pris en argument par la méthode normalAt()
     */
    private int getAlt(double i, double j) {
        return buffer.get((int) (size / 2d - (j + 1) * (side + 1) + i));
    }

    /**
     * @param p
     *            dont on vérifie l'appartenance à la zone couverte par le MNT
     * 
     * @return false si le point pris en argument ne fait pas partie de la zone
     *         couverte par le MNT, true sinon
     */
    private boolean IsInDomain(PointGeo p) {
        if (p.longitude() < SWpointLong || p.longitude() > SWpointLong + 1
                || p.latitude() < SWpointLat || p.latitude() > SWpointLat + 1)
            return false;
        else
            return true;
    }

    /**
     * @param file
     *            le fichier HGT contenant le modèle numérique du terrain
     * 
     * @return true si le nom du fichier respecte les conventions de nommmage
     *         d'un fichier HGT
     */
    private boolean respectConventions(File file) {
        // Vérification que la taille du fichier, en octet, a bien une racine
        // entière.
        // Pour cela calcule une première fois la racine, puis on fait la
        // transformation inverse et on compare avec la valeur de départ
        if (Math.pow((int) Math.sqrt(file.length() / 2), 2) * 2 != file
                .length())
            return false;
        // Le nom est régulier s'il vérifie l'expression régulière d'un fichier
        // hgt. Par exemple "[NS]" signifie que la première lettre (ici)
        // appartient à la
        // classe [NS] : soit N, soit S.
        else if (!file.getName().matches("[NS][0-9]*{3}[EW][0-9]*{2}" + ".hgt"))
            return false;
        else
            return true;
    }
}
