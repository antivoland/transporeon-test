package antivoland.transporeon.model.segmentation.slice;

import antivoland.transporeon.model.segmentation.Segmentation;
import antivoland.transporeon.model.segmentation.segment.SouthPoleSegment;

import java.util.Collection;
import java.util.List;

public class SouthPoleSlice extends Slice<SouthPoleSegment> {
    private final SouthPoleSegment segment = new SouthPoleSegment(this);

    public SouthPoleSlice(int southernmostLat, int northernmostLat, Segmentation segmentation) {
        super(SliceType.SOUTH_POLE, southernmostLat, northernmostLat, segmentation);
    }

    @Override
    public SouthPoleSegment segmentFor(int lon) {
        return segment;
    }

    @Override
    protected Collection<SouthPoleSegment> segments() {
        return List.of(segment);
    }

    @Override
    public String toString() {
        return "SouthPoleSlice(" +
                "southernmostLat=" + southernmostLat +
                ", northernmostLat=" + northernmostLat +
                ")";
    }
}