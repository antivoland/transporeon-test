package antivoland.transporeon.model.change;

import antivoland.transporeon.dataset.AirportDataset;
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

import static antivoland.transporeon.model.DistanceCalculator.kmDistance;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class ChangeDetectorTest {
    static final double MAX_DISTANCE_KM = 100;

    @Autowired
    AirportDataset airportDataset;

    @Test
    void test() {
        AtomicInteger nextId = new AtomicInteger(0);
        Map<Integer, Spot> spots = airportDataset
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

        leftDiff(bruteForceChangeIds, segmentationBasedChangeIds, spots)
                .forEach(change -> log.error("Brute force only change: {}", change));

        leftDiff(segmentationBasedChangeIds, bruteForceChangeIds, spots)
                .forEach(change -> log.error("Segmentation based only change: {}", change));

        assertThat(segmentationBasedChangeIds.equals(bruteForceChangeIds))
                .withFailMessage(() -> "Brute force and a segmentation-based approach produce different changes")
                .isTrue(); // todo: few corner spots are not detected as changes
    }

    static Set<ChangeId> detectChanges(Supplier<ChangeDetector> detector, Collection<Spot> spots) {
        Collection<Change> changes = detector.get().detect(spots, MAX_DISTANCE_KM);
        assertThat(changes).hasSizeGreaterThan(0);
        changes.forEach(change -> assertThat(change.kmDistance).isLessThan(MAX_DISTANCE_KM));
        Set<ChangeId> changeIds = changes.stream().map(ChangeId::new).collect(toSet());
        assertThat(changeIds).hasSameSizeAs(changes);
        return changeIds;
    }

    static Collection<Change> leftDiff(Set<ChangeId> left, Set<ChangeId> right, Map<Integer, Spot> spots) {
        return left
                .stream()
                .filter(changeId -> !right.contains(changeId))
                .map(changeId -> changeId.change(spots))
                .toList();
    }

    @ToString
    @EqualsAndHashCode
    static class ChangeId {
        final int srcId;
        final int dstId;

        ChangeId(Change change) {
            this(change.src.id, change.dst.id);
        }

        ChangeId(int srcId, int dstId) {
            this.srcId = srcId;
            this.dstId = dstId;
        }

        Change change(Map<Integer, Spot> spots) {
            Spot src = spots.get(srcId);
            Spot dst = spots.get(dstId);
            return new Change(src, dst, kmDistance(src, dst));
        }
    }
}