package antivoland.transporeon.model.change;

import antivoland.transporeon.model.Spot;
import antivoland.transporeon.model.segmentation.Segmentation;
import antivoland.transporeon.model.segmentation.segment.Segment;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static antivoland.transporeon.model.DistanceCalculator.kmDistance;

public class SegmentationBasedChangeDetector implements ChangeDetector {
    @Override
    public Collection<Change> detect(Collection<Spot> spots, double kmMaxDistance) {
        Segmentation segmentation = new Segmentation();
        spots.forEach(spot -> segmentation.segmentFor(spot.lat, spot.lon).spots.add(spot));

        Set<Change> changes = new HashSet<>();
        segmentation.segments().forEach(segment -> {
            Collection<Segment> env = segment.environment(kmMaxDistance);
            for (int srcIdx = 0; srcIdx < segment.spots.size(); ++srcIdx) {
                Spot src = segment.spots.get(srcIdx);
                for (int dstIdx = srcIdx + 1; dstIdx < segment.spots.size(); ++dstIdx) {
                    Spot dst = segment.spots.get(dstIdx);
                    double kmDistance = kmDistance(src, dst);
                    if (kmDistance < kmMaxDistance) {
                        changes.add(new Change(src, dst, kmDistance));
                        changes.add(new Change(dst, src, kmDistance));
                    }
                }
                env.forEach(envSegment -> {
                    for (int dstIdx = 0; dstIdx < envSegment.spots.size(); ++dstIdx) {
                        Spot dst = envSegment.spots.get(dstIdx);
                        double kmDistance = kmDistance(src, dst);
                        if (kmDistance < kmMaxDistance) {
                            changes.add(new Change(src, dst, kmDistance));
                        }
                    }
                });
            }
        });
        return changes;
    }
}