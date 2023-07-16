package antivoland.transporeon.model;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

public class DistanceCalculator {
    private static final GeodeticCalculator GEODETIC_CALCULATOR = new GeodeticCalculator();

    public static double kmDistance(Spot srcSpot, Spot dstSpot) {
        return kmDistance(srcSpot.lat, srcSpot.lon, dstSpot.lat, dstSpot.lon);
    }

    public static double kmDistance(double[] srcLatLon, double[] dstLatLon) {
        return kmDistance(srcLatLon[0], srcLatLon[1], dstLatLon[0], dstLatLon[1]);
    }

    public static double kmDistance(double srcLat, double srcLon, double dstLat, double dstLon) {
        GeodeticCurve curve = GEODETIC_CALCULATOR.calculateGeodeticCurve(
                Ellipsoid.WGS84,
                new GlobalCoordinates(srcLat, srcLon),
                new GlobalCoordinates(dstLat, dstLon));
        return curve.getEllipsoidalDistance() / 1000;
    }
}