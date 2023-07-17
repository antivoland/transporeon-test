package antivoland.transporeon.model.segmentation;

import antivoland.transporeon.model.segmentation.segment.Segment;
import antivoland.transporeon.model.segmentation.slice.NorthPoleSlice;
import antivoland.transporeon.model.segmentation.slice.RegularSlice;
import antivoland.transporeon.model.segmentation.slice.Slice;
import antivoland.transporeon.model.segmentation.slice.SouthPoleSlice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.IntStream.range;

public class Segmentation {
    private static final int POLE_SIZE_DEGREES = 15;

    private final List<Slice<? extends Segment>> slices = new ArrayList<>();

    public Segmentation() {
        slices.add(new SouthPoleSlice(-90, -90 + POLE_SIZE_DEGREES, this));
        range(-90 + POLE_SIZE_DEGREES, 90 - POLE_SIZE_DEGREES)
                .mapToObj(southernmostLat -> new RegularSlice(southernmostLat, southernmostLat + 1, this))
                .forEach(slices::add);
        slices.add(new NorthPoleSlice(90 - POLE_SIZE_DEGREES, 90, this));
    }

    public Segment segmentFor(double lat, double lon) {
        return sliceFor(lat).segmentFor(lon);
    }

    private Slice<? extends Segment> sliceFor(double lat) {
        return sliceFor((int) lat);
    }

    /*
     180
     ...
     165 <- north pole (165<=lat+90<=180)
     ...
     42 <- regular (42<=lat+90<43)
     ...
     15 <- south pole (0<=lat+90<15)
     ...
     0
     */
    public Slice<? extends Segment> sliceFor(int lat) {
        int shifted = 90 + lat;
        if (shifted < POLE_SIZE_DEGREES) return slices.get(0);
        if (shifted >= 180 - POLE_SIZE_DEGREES) return slices.get(slices.size() - 1);
        return slices.get(1 + shifted - POLE_SIZE_DEGREES);
    }

    public Collection<? extends Segment> segments() {
        return slices
                .stream()
                .map(Slice::nonEmptySegments)
                .flatMap(Collection::stream)
                .toList();
    }
}