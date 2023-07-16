package antivoland.transporeon.model.segmentation;

import java.util.*;

import static antivoland.transporeon.model.DistanceCalculator.kmDistance;
import static antivoland.transporeon.model.segmentation.SliceType.REGULAR_SLICE;

class RegularSlice extends Slice {
    private final Map<Integer, Segment> segments = new HashMap<>();

    RegularSlice(int southernmostLat, int northernmostLat) {
        super(REGULAR_SLICE, southernmostLat, northernmostLat);
    }

    @Override
    Segment segmentFor(int lon) {
        int shifted = 180 + lon;
        Segment segment = segments.get(shifted);
        if (segment == null) {
            segment = new Segment(this, lon, lon + 1);
            segments.put(shifted, segment);
        }
        return segment;
    }

    @Override
    Collection<Segment> segmentsWestOf(int lon, double kmDelta) {
        List<Segment> segments = new ArrayList<>();
        int westernLon = (lon - 1) % 360;
        Segment western;
        double kmDistance;
        do {
            western = segmentFor(westernLon);
            segments.add(western);
            kmDistance = Math.min(
                    kmDistance(lon, southernmostLat, western.westernmostLon(), southernmostLat),
                    kmDistance(lon, northernmostLat, western.westernmostLon(), northernmostLat));
            westernLon = (westernLon - 1) % 360;
        } while (kmDistance < kmDelta);
        return segments;
    }

    @Override
    Collection<Segment> segmentsEastOf(int lon, double kmDelta) {
        List<Segment> segments = new ArrayList<>();
        int easternLon = lon;
        Segment eastern;
        double kmDistance;
        do {
            eastern = segmentFor(easternLon);
            segments.add(eastern);
            kmDistance = Math.min(
                    kmDistance(lon, southernmostLat, eastern.easternmostLon(), southernmostLat),
                    kmDistance(lon, northernmostLat, eastern.easternmostLon(), northernmostLat));
            easternLon = (easternLon + 1) % 360;
        } while (kmDistance < kmDelta);
        return segments;
    }

    @Override
    Collection<Segment> segments() {
        return segments.values();
    }

    @Override
    public String toString() {
        return "RegularSlice(" +
                "southernmostLat=" + southernmostLat +
                ", northernmostLat=" + northernmostLat +
                ", numberOfSegments=" + segments.size() +
                ")";
    }
}