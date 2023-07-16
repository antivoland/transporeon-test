package antivoland.transporeon.model.segmentation;

import java.util.List;
import java.util.stream.IntStream;

public class Segmentation {
    private static final int POLE_SIZE_DEGREES = 15;

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
    private final Slice southPole = new PoleSlice(SliceType.SOUTH_POLE);
    private final Slice northPole = new PoleSlice(SliceType.NORTH_POLE);
    private final List<Slice> regularSlices = IntStream
            .range(POLE_SIZE_DEGREES, 180 - POLE_SIZE_DEGREES)
            .mapToObj(id -> (Slice) new RegularSlice())
            .toList();

    private Slice sliceFor(double lat) {
        int id = 90 + (int) lat;
        if (id < POLE_SIZE_DEGREES) return southPole;
        if (id >= 180 - POLE_SIZE_DEGREES) return northPole;
        return regularSlices.get(id - POLE_SIZE_DEGREES);
    }

    public Segment segmentFor(double lat, double lon) {
        return sliceFor(lat).segmentFor(lon);
    }

    public List<Segment> environment(Segment segment, double kmRadius) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}