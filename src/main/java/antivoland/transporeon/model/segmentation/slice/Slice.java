package antivoland.transporeon.model.segmentation.slice;

import antivoland.transporeon.model.segmentation.Segmentation;
import antivoland.transporeon.model.segmentation.segment.Segment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static antivoland.transporeon.model.DistanceCalculator.kmDistance;

public abstract class Slice<SEGMENT extends Segment> {
    public final SliceType type;
    public final int southernmostLat;
    public final int northernmostLat;
    protected final Segmentation segmentation;

    protected Slice(SliceType type, int southernmostLat, int northernmostLat, Segmentation segmentation) {
        this.type = type;
        this.southernmostLat = southernmostLat;
        this.northernmostLat = northernmostLat;
        this.segmentation = segmentation;
    }

    public final SEGMENT segmentFor(double lon) {
        return segmentFor((int) lon);
    }

    public abstract SEGMENT segmentFor(int lon);

    public abstract Collection<SEGMENT> segments();

    public final Collection<Slice<?>> southernSlices(double kmDelta) {
        if (type == SliceType.SOUTH_POLE) return List.of();
        Collection<Slice<?>> slices = new ArrayList<>();
        int lat = southernmostLat - 1;
        Slice<?> slice = segmentation.sliceFor(lat);
        while (kmDistanceToSouthernSlice(slice) < kmDelta) {
            slices.add(slice);
            if (slice.type == SliceType.SOUTH_POLE) return slices;
            --lat;
            slice = segmentation.sliceFor(lat);

        }
        return slices;
    }

    private double kmDistanceToSouthernSlice(Slice<?> slice) {
        return kmDistance(southernmostLat, 0, slice.northernmostLat, 0);
    }

    public final Collection<Slice<?>> northernSlices(double kmDelta) {
        if (type == SliceType.NORTH_POLE) return List.of();
        Collection<Slice<?>> slices = new ArrayList<>();
        int lat = northernmostLat;
        Slice<?> slice = segmentation.sliceFor(lat);
        while (kmDistanceToNorthernSlice(slice) < kmDelta) {
            slices.add(slice);
            if (slice.type == SliceType.NORTH_POLE) return slices;
            ++lat;
            slice = segmentation.sliceFor(lat);
        }
        return slices;
    }

    private double kmDistanceToNorthernSlice(Slice<?> slice) {
        return kmDistance(northernmostLat, 0, slice.southernmostLat, 0);
    }
}