package antivoland.transporeon.model.segmentation;

import java.util.Collection;
import java.util.List;

class PoleSlice extends Slice {
    private final Segment segment = new Segment(this);

    PoleSlice(SliceType type, int southernmostLat, int northernmostLat) {
        super(type, southernmostLat, northernmostLat);
    }

    @Override
    Segment segmentFor(int lon) {
        return segment;
    }

    @Override
    Collection<Segment> segmentsWestOf(int lon, double kmDelta) {
        return List.of();
    }

    @Override
    Collection<Segment> segmentsEastOf(int lon, double kmDelta) {
        return List.of();
    }

    @Override
    Collection<Segment> segments() {
        return List.of(segment);
    }

    @Override
    public String toString() {
        return "PoleSlice(" +
                "southernmostLat=" + southernmostLat +
                ", northernmostLat=" + northernmostLat +
                ")";
    }
}