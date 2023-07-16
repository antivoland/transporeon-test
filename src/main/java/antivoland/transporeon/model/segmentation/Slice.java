package antivoland.transporeon.model.segmentation;

import java.util.Collection;

abstract class Slice {
    final SliceType type;
    final int southernmostLat;
    final int northernmostLat;

    Slice(SliceType type, int southernmostLat, int northernmostLat) {
        this.type = type;
        this.southernmostLat = southernmostLat;
        this.northernmostLat = northernmostLat;
    }

    Segment segmentFor(double lon) {
        return segmentFor((int) lon);
    }

    abstract Segment segmentFor(int lon);

    abstract Collection<Segment> segmentsWestOf(int lon, double kmDelta);

    abstract Collection<Segment> segmentsEastOf(int lon, double kmDelta);

    abstract Collection<Segment> segments();
}