package antivoland.transporeon.model.segmentation;

import java.util.HashMap;
import java.util.Map;

class RegularSlice extends Slice {
    private final Map<Integer, Segment> sectors = new HashMap<>();

    RegularSlice() {
        super(SliceType.REGULAR);
    }

    @Override
    Segment segmentFor(double lon) {
        int id = 180 + (int) lon;
        Segment segment = sectors.get(id);
        if (segment == null) {
            segment = new Segment();
            sectors.put(id, segment);
        }
        return segment;
    }
}