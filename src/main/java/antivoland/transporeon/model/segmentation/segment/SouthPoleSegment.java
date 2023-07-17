package antivoland.transporeon.model.segmentation.segment;

import antivoland.transporeon.model.segmentation.slice.SouthPoleSlice;

import java.util.ArrayList;
import java.util.Collection;

public class SouthPoleSegment extends PoleSegment {
    private final SouthPoleSlice slice;

    public SouthPoleSegment(SouthPoleSlice slice) {
        this.slice = slice;
    }

    @Override
    public Collection<Segment> environment(double kmDelta) {
        Collection<Segment> segments = new ArrayList<>();
        slice.northernSlices(kmDelta).forEach(slice -> segments.addAll(slice.nonEmptySegments()));
        return segments;
    }

    @Override
    public String toString() {
        return "SouthPoleSegment(" +
                "numberOfSpots=" + spots.size() +
                ")";
    }
}