package antivoland.transporeon.model.change;

import antivoland.transporeon.model.Spot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static antivoland.transporeon.model.DistanceCalculator.kmDistance;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.IntStream.range;

public class BruteForceChangeDetector implements ChangeDetector {
    @Override
    public Set<Change> detect(List<Spot> spots, double kmMaxDistance) {
        return range(0, spots.size())
                .parallel()
                .mapToObj(srcIdx -> {
                    Spot src = spots.get(srcIdx);
                    Collection<Change> batch = new ArrayList<>();
                    for (int dstIdx = srcIdx + 1; dstIdx < spots.size(); ++dstIdx) {
                        Spot dst = spots.get(dstIdx);
                        if (kmDistance(src, dst) < kmMaxDistance) {
                            batch.add(new Change(src.id, dst.id));
                            batch.add(new Change(dst.id, src.id));
                        }
                    }
                    return batch;
                })
                .flatMap(Collection::stream)
                .collect(toSet());
    }
}