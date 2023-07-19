package antivoland.transporeon.model.change;

import antivoland.transporeon.model.Spot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static antivoland.transporeon.model.DistanceCalculator.kmDistance;
import static java.util.stream.IntStream.range;

public class BruteForceChangeDetector implements ChangeDetector {
    @Override
    public Collection<Change> detect(Collection<Spot> spots, double kmMaxDistance) {
        return detect(new ArrayList<>(spots), kmMaxDistance);
    }

    private List<Change> detect(List<Spot> spots, double kmMaxDistance) {
        return range(0, spots.size())
                .parallel()
                .mapToObj(srcIdx -> {
                    Spot src = spots.get(srcIdx);
                    Collection<Change> batch = new ArrayList<>();
                    for (int dstIdx = srcIdx + 1; dstIdx < spots.size(); ++dstIdx) {
                        Spot dst = spots.get(dstIdx);
                        double kmDistance = kmDistance(src, dst);
                        if (kmDistance < kmMaxDistance) {
                            batch.add(new Change(src, dst, kmDistance));
                            batch.add(new Change(dst, src, kmDistance));
                        }
                    }
                    return batch;
                })
                .flatMap(Collection::stream)
                .toList();
    }
}