package antivoland.transporeon.model.segmentation.slice;

import antivoland.transporeon.model.segmentation.Segmentation;
import antivoland.transporeon.model.segmentation.segment.NorthPoleSegment;

import java.util.Collection;
import java.util.List;

public class NorthPoleSlice extends Slice<NorthPoleSegment> {
    private final NorthPoleSegment segment = new NorthPoleSegment(this);

    public NorthPoleSlice(int southernmostLat, int northernmostLat, Segmentation segmentation) {
        super(SliceType.NORTH_POLE, southernmostLat, northernmostLat, segmentation);
    }

    @Override
    public NorthPoleSegment segmentFor(int lon) {
        return segment;
    }

    @Override
    public Collection<NorthPoleSegment> segments() {
        return List.of(segment);
    }

    @Override
    public String toString() {
        return "NorthPoleSlice(" +
                "southernmostLat=" + southernmostLat +
                ", northernmostLat=" + northernmostLat +
                ")";
    }
}