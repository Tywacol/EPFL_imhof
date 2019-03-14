package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;

import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

/**
 * Représente le générateur de peintre de réseau routier.
 * 
 * @author Pierre Gabioud (247216)
 * @author Corto Callerisa (251769)
 */
public final class RoadPainterGenerator {
    private final static Predicate<Attributed<?>> isBridge = Filters.tagged("bridge");
    private final static Predicate<Attributed<?>> isTunnel = Filters.tagged("tunnel");
    private final static Predicate<Attributed<?>> isBasic = isBridge.or(isTunnel)
            .negate();

    private RoadPainterGenerator() {
    }

    /**
     * @param roadSpec
     *            nombre variable de spécifications de routes
     * 
     * @return le peintre pour le réseau routier correspondant
     */

    public static Painter painterForRoads(RoadSpec... roadSpec) {
        return (x, y) -> {
            Painter tunnel = null, normalOut = null, normalIn = null, bridgeOut = null, bridgeIn = null;
            boolean firstIt = true;
            for (RoadSpec road : roadSpec) {

                tunnel = firstIt ? road.tunnelP : tunnel.above(road.tunnelP);
                normalOut = firstIt ? road.normRoadOutP : normalOut
                        .above(road.normRoadOutP);
                normalIn = firstIt ? road.normRoadInP : normalIn
                        .above(road.normRoadInP);
                bridgeOut = firstIt ? road.bridgeOutP : bridgeOut
                        .above(road.bridgeOutP);
                bridgeIn = firstIt ? road.bridgeInP : bridgeIn
                        .above(road.bridgeInP);
                firstIt = false;
            }
            Painter superPainter = bridgeIn.above(bridgeOut).above(normalIn)
                    .above(normalOut).above(tunnel);
            superPainter.drawMap(x, y);
        };
    }

    /**
     * Décrit le dessin d'un type de route donné.
     */
    public final static class RoadSpec {
        private final Predicate<Attributed<?>> predicate;

        private final Painter tunnelP, normRoadOutP, normRoadInP, bridgeOutP,
                bridgeInP;

        /**
         * @param predicate
         *            un prédicat filtrant les types de routes
         * 
         * @param wI
         *            la largeur du trait intérieur
         * 
         * @param cI
         *            la couleur de du trait intérieur
         * 
         * @param wC
         *            la largeur du trait extérieur
         * 
         * @param cC
         *            la couleur du trait extérieur
         */
        public RoadSpec(Predicate<Attributed<?>> predicate, float wI, Color cI,
                float wC, Color cC) {

            this.predicate = predicate;
            tunnelP = Painter
                    .line(wI / 2f, cC, LineCap.Butt, LineJoin.Round, 2f * wI,
                            2f * wI).when(predicate).when(isTunnel);
            normRoadOutP = Painter
                    .line(wI + 2f * wC, cC, LineCap.Round, LineJoin.Round, null)
                    .when(predicate).when(isBasic);
            normRoadInP = Painter
                    .line(wI, cI, LineCap.Round, LineJoin.Round, null)
                    .when(predicate).when(isBasic);
            bridgeOutP = Painter
                    .line(wI + 2f * wC, cC, LineCap.Butt, LineJoin.Round, null)
                    .when(predicate).when(isBridge);
            bridgeInP = Painter
                    .line(wI, cI, LineCap.Round, LineJoin.Round, null)
                    .when(predicate).when(isBridge);

        }

        /**
         * @return le prédicat
         */
        public Predicate<Attributed<?>> predicate() {
            return predicate;
        }
    }
}
