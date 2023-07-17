package antivoland.transporeon.model.segmentation.segment;

import java.util.Collection;
import java.util.List;

abstract class PoleSegment extends Segment {
    @Override
    protected Collection<Segment> sameSliceWesternSegments(double kmDelta) {
        return List.of();
    }

    @Override
    protected Collection<Segment> sameSliceEasternSegments(double kmDelta) {
        return List.of();
    }
}