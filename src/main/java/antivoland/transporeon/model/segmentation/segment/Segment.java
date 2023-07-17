package antivoland.transporeon.model.segmentation.segment;

import antivoland.transporeon.model.Spot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Segment {
    public final List<Spot> spots = new ArrayList<>();

    public abstract Collection<Segment> environment(double kmDelta);

    protected abstract Collection<Segment> sameSliceWesternSegments(double kmDelta);

    protected abstract Collection<Segment> sameSliceEasternSegments(double kmDelta);
}