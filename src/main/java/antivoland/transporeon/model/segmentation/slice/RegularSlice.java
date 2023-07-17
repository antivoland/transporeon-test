package antivoland.transporeon.model.segmentation.slice;

import antivoland.transporeon.model.segmentation.Segmentation;
import antivoland.transporeon.model.segmentation.segment.RegularSegment;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RegularSlice extends Slice<RegularSegment> {
    private final Map<Integer, RegularSegment> segments = new HashMap<>();

    public RegularSlice(int southernmostLat, int northernmostLat, Segmentation segmentation) {
        super(SliceType.REGULAR, southernmostLat, northernmostLat, segmentation);
    }

    @Override
    public RegularSegment segmentFor(int lon) {
        int shifted = (180 + lon) % 360;
        if (shifted < 0) shifted = 360 - shifted;
        RegularSegment segment = segments.get(shifted);
        if (segment == null) {
            segment = new RegularSegment(lon, lon + 1, this);
            segments.put(shifted, segment);
        }
        return segment;
    }

    @Override
    protected Collection<RegularSegment> segments() {
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