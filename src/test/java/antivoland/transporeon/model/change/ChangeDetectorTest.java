package antivoland.transporeon.model.change;

import antivoland.transporeon.dataset.AirportsDataset;
import antivoland.transporeon.dataset.Dataset;
import antivoland.transporeon.model.Spot;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static antivoland.transporeon.model.DistanceCalculator.kmDistance;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class ChangeDetectorTest {
    private static final double MAX_DISTANCE_KM = 100;

    @Autowired
    AirportsDataset airportsDataset;

    @Test
    void test() {
        AtomicInteger nextId = new AtomicInteger(0);
        Map<Integer, Spot> spots = airportsDataset
                .read()
                .filter(airport -> !airport.codes().isEmpty())
                .map(airport -> airport.spot(nextId.incrementAndGet()))
                .collect(toMap(spot -> spot.id, identity()));
        log.info("Dataset: size={}", spots.size());

        long start = System.currentTimeMillis();
        Set<ChangeId> bruteForceChangeIds = detectChanges(BruteForceChangeDetector::new, spots.values());
        log.info("Brute force: changes={}, duration={}ms", bruteForceChangeIds.size(), System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        Set<ChangeId> segmentationBasedChangeIds = detectChanges(SegmentationBasedChangeDetector::new, spots.values());
        log.info("Segmentation based: changes={}, duration={}ms", segmentationBasedChangeIds.size(), System.currentTimeMillis() - start);

        leftDiff(segmentationBasedChangeIds, bruteForceChangeIds, spots)
                .forEach(change -> log.info("Segmentation based only change: {}", change));

        leftDiff(bruteForceChangeIds, segmentationBasedChangeIds, spots)
                .forEach(change -> log.info("Brute force only change: {}", change));

        // todo: few corner spots are not detected as changes
    }

    private static Set<ChangeId> detectChanges(Supplier<ChangeDetector> detector, Collection<Spot> spots) {
        Collection<Change> changes = detector.get().detect(spots, MAX_DISTANCE_KM);
        assertThat(changes).hasSizeGreaterThan(0);
        changes.forEach(change -> assertThat(change.kmDistance).isLessThan(MAX_DISTANCE_KM));
        Set<ChangeId> changeIds = changes.stream().map(ChangeId::new).collect(toSet());
        assertThat(changeIds).hasSameSizeAs(changes);
        return changeIds;
    }

    private static Collection<Change> leftDiff(Set<ChangeId> left, Set<ChangeId> right, Map<Integer, Spot> spots) {
        return left
                .stream()
                .filter(changeId -> !right.contains(changeId))
                .map(changeId -> changeId.change(spots))
                .toList();
    }

    Stream<Dataset.Airport> airports() {
        return airportsDataset.read().filter(airport -> !airport.codes().isEmpty());
    }

    @ToString
    @EqualsAndHashCode
    static class ChangeId {
        final int srcId;
        final int dstId;

        public ChangeId(Change change) {
            this.srcId = change.src.id;
            this.dstId = change.dst.id;
        }

        Change change(Map<Integer, Spot> spots) {
            Spot src = spots.get(srcId);
            Spot dst = spots.get(dstId);
            return new Change(src, dst, kmDistance(src, dst));
        }
    }
}