package antivoland.transporeon.model.segmentation.segment;

import antivoland.transporeon.model.segmentation.slice.RegularSlice;

import java.util.ArrayList;
import java.util.Collection;

import static antivoland.transporeon.model.DistanceCalculator.kmDistance;

public class RegularSegment extends Segment {
    public final int westernmostLon;
    public final int easternmostLon;
    private final RegularSlice slice;

    public RegularSegment(int westernmostLon, int easternmostLon, RegularSlice slice) {
        this.westernmostLon = westernmostLon;
        this.easternmostLon = easternmostLon;
        this.slice = slice;
    }

    @Override
    public Collection<Segment> environment(double kmDelta) {
        Collection<Segment> segments = new ArrayList<>();
        slice.southernSlices(kmDelta).forEach(slice -> {
            Segment segment = slice.segmentFor(westernmostLon);
            segments.addAll(segment.sameSliceWesternSegments(kmDelta));
            segments.add(segment);
            segments.addAll(segment.sameSliceEasternSegments(kmDelta));
        });

        segments.addAll(sameSliceWesternSegments(kmDelta));
        segments.addAll(sameSliceEasternSegments(kmDelta));

        slice.northernSlices(kmDelta).forEach(slice -> {
            Segment segment = slice.segmentFor(westernmostLon);
            segments.addAll(segment.sameSliceWesternSegments(kmDelta));
            segments.add(segment);
            segments.addAll(segment.sameSliceEasternSegments(kmDelta));
        });
        return segments;
    }

    @Override
    protected Collection<Segment> sameSliceWesternSegments(double kmDelta) {
        Collection<Segment> segments = new ArrayList<>();
        int lon = (westernmostLon - 1) % 360;
        RegularSegment segment = slice.segmentFor(lon);
        while (kmDistanceToSameSliceWesternSegment(segment) < kmDelta) {
            segments.add(segment);
            lon = (lon - 1) % 360;
            segment = slice.segmentFor(lon);
        }
        return segments;
    }

    private double kmDistanceToSameSliceWesternSegment(RegularSegment segment) {
        return Math.min(
                kmDistance(slice.southernmostLat, westernmostLon, slice.southernmostLat, segment.easternmostLon),
                kmDistance(slice.northernmostLat, westernmostLon, slice.northernmostLat, segment.easternmostLon));
    }

    @Override
    protected Collection<Segment> sameSliceEasternSegments(double kmDelta) {
        Collection<Segment> segments = new ArrayList<>();
        int lon = easternmostLon;
        RegularSegment segment = slice.segmentFor(lon);
        while (kmDistanceToSameSliceEasternSegment(segment) < kmDelta) {
            segments.add(segment);
            lon = (lon + 1) % 360;
            segment = slice.segmentFor(lon);
        }
        return segments;
    }

    private double kmDistanceToSameSliceEasternSegment(RegularSegment segment) {
        return Math.min(
                kmDistance(slice.southernmostLat, easternmostLon, slice.southernmostLat, segment.westernmostLon),
                kmDistance(slice.northernmostLat, easternmostLon, slice.northernmostLat, segment.westernmostLon));
    }

    @Override
    public String toString() {
        return "RegularSegment(" +
                "numberOfSpots=" + spots.size() +
                ", westernmostLon=" + westernmostLon +
                ", easternmostLon=" + easternmostLon +
                ")";
    }
}