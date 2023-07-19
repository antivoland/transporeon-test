package antivoland.transporeon.model;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

public class DistanceCalculator {
    private static final GeodeticCalculator GEODETIC_CALCULATOR = new GeodeticCalculator();

    public static double kmDistance(Spot src, Spot dst) {
        return kmDistance(src.lat, src.lon, dst.lat, dst.lon);
    }

    public static double kmDistance(double srcLat, double srcLon, double dstLat, double dstLon) {
        GeodeticCurve curve = GEODETIC_CALCULATOR.calculateGeodeticCurve(
                Ellipsoid.WGS84,
                new GlobalCoordinates(srcLat, srcLon),
                new GlobalCoordinates(dstLat, dstLon));
        return curve.getEllipsoidalDistance() / 1000;
    }
}