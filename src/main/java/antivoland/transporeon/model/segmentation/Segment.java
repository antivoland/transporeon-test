package antivoland.transporeon.model.segmentation;

import antivoland.transporeon.model.Spot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Segment {
    public final List<Spot> spots = new ArrayList<>();
    final Slice slice;
    private final Supplier<Integer> westernmostLon;
    private final Supplier<Integer> easternmostLon;

    Segment(Slice slice) {
        this(slice, unsupported(), unsupported());
    }

    Segment(Slice slice, int westernmostLon, int easternmostLon) {
        this(slice, () -> westernmostLon, () -> easternmostLon);
    }

    Segment(Slice slice, Supplier<Integer> westernmostLon, Supplier<Integer> easternmostLon) {
        this.slice = slice;
        this.westernmostLon = westernmostLon;
        this.easternmostLon = easternmostLon;
    }

    public int southernmostLat() {
        return slice.southernmostLat;
    }

    public int northernmostLat() {
        return slice.northernmostLat;
    }

    public int westernmostLon() {
        return westernmostLon.get();
    }

    public int easternmostLon() {
        return easternmostLon.get();
    }



    @Override
    public String toString() {
        return switch (slice.type) {
            case SOUTH_POLE, NORTH_POLE -> "PoleSegment(" +
                    "southernmostLat=" + southernmostLat() +
                    ", northernmostLat=" + northernmostLat() +
                    ", numberOfSpots=" + spots.size() +
                    ")";
            case REGULAR_SLICE -> "RegularSegment(" +
                    "southernmostLat=" + southernmostLat() +
                    ", northernmostLat=" + northernmostLat() +
                    ", westernmostLon=" + westernmostLon() +
                    ", easternmostLon=" + easternmostLon() +
                    ", numberOfSpots=" + spots.size() +
                    ")";
        };
    }

    private static Supplier<Integer> unsupported() {
        return () -> {
            throw new UnsupportedOperationException("Method not supported");
        };
    }
}