package antivoland.transporeon.model.segmentation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static antivoland.transporeon.model.DistanceCalculator.kmDistance;
import static antivoland.transporeon.model.segmentation.SliceType.*;
import static java.util.stream.IntStream.range;

public class Segmentation {
    private static final int POLE_SIZE_DEGREES = 15;

    private final List<Slice> slices = new ArrayList<>();

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
    public Segmentation() {
        slices.add(new PoleSlice(SOUTH_POLE, 0, POLE_SIZE_DEGREES));
        range(POLE_SIZE_DEGREES, 180 - POLE_SIZE_DEGREES)
                .mapToObj(southernmostLat -> (Slice) new RegularSlice(southernmostLat, southernmostLat + 1))
                .forEach(slices::add);
        slices.add(new PoleSlice(SliceType.NORTH_POLE, 180 - POLE_SIZE_DEGREES, 180));
    }

    public Segment segmentFor(double lat, double lon) {
        return sliceFor(lat).segmentFor(lon);
    }

    private Slice sliceFor(double lat) {
        return sliceFor((int) lat);
    }

    private Slice sliceFor(int lat) {
        int shifted = 90 + lat;
        if (shifted < POLE_SIZE_DEGREES) return slices.get(0);
        if (shifted >= 180 - POLE_SIZE_DEGREES) return slices.get(slices.size() - 1);
        return slices.get(1 + shifted - POLE_SIZE_DEGREES);
    }

    public List<Segment> segmentEnvironment(Segment segment, double kmRadius) {
        List<Segment> segments = new ArrayList<>();
        slicesSouthOf(segment.slice, kmRadius).forEach(slice -> {
            segments.addAll(slice.segmentsWestOf(segment.westernmostLon(), kmRadius));
            segments.add(slice.segmentFor(segment.westernmostLon()));
            segments.addAll(slice.segmentsEastOf(segment.easternmostLon(), kmRadius));
        });

        if (segment.slice.type == REGULAR_SLICE) {
            segments.addAll(segment.slice.segmentsWestOf(segment.westernmostLon(), kmRadius));
            segments.addAll(segment.slice.segmentsEastOf(segment.easternmostLon(), kmRadius));
        }

        slicesNorthOf(segment.slice, kmRadius).forEach(slice -> {
            segments.addAll(slice.segmentsWestOf(segment.westernmostLon(), kmRadius));
            segments.add(slice.segmentFor(segment.westernmostLon()));
            segments.addAll(slice.segmentsEastOf(segment.easternmostLon(), kmRadius));
        });

        return segments;
    }

    private List<Slice> slicesSouthOf(Slice slice, double kmDelta) {
        if (slice.type == SOUTH_POLE) return List.of();
        List<Slice> slices = new ArrayList<>();
        int southernLat = slice.southernmostLat - 1;
        Slice southern;
        double kmDistance;
        do {
            southern = sliceFor(southernLat);
            slices.add(southern);
            kmDistance = kmDistance(slice.southernmostLat, 0, southern.northernmostLat, 0);
            --southernLat;
        } while (southern.type != SOUTH_POLE && kmDistance < kmDelta);
        return slices;
    }

    private List<Slice> slicesNorthOf(Slice slice, double kmDelta) {
        if (slice.type == NORTH_POLE) return List.of();
        List<Slice> slices = new ArrayList<>();
        int northernLat = slice.northernmostLat;
        Slice northern;
        double kmDistance;
        do {
            northern = sliceFor(northernLat);
            slices.add(northern);
            ++northernLat;
            kmDistance = kmDistance(slice.southernmostLat, 0, northern.northernmostLat, 0);
        } while (northern.type != NORTH_POLE && kmDistance < kmDelta);
        return slices;
    }

    public Stream<Segment> segments() {
        return slices.stream().flatMap(slice -> slice.segments().stream());
    }
}