package antivoland.transporeon.model.segmentation.segment;

import antivoland.transporeon.model.segmentation.slice.NorthPoleSlice;

import java.util.ArrayList;
import java.util.Collection;

public class NorthPoleSegment extends PoleSegment {
    private final NorthPoleSlice slice;

    public NorthPoleSegment(NorthPoleSlice slice) {
        this.slice = slice;
    }

    @Override
    public Collection<Segment> environment(double kmDelta) {
        Collection<Segment> segments = new ArrayList<>();
        slice.southernSlices(kmDelta).forEach(slice -> segments.addAll(slice.segments()));
        return segments;
    }

    @Override
    public String toString() {
        return "NorthPoleSegment(" +
                "numberOfSpots=" + spots.size() +
                ")";
    }
}