package antivoland.transporeon.model.segmentation;

import antivoland.transporeon.dataset.AirportsDataset;
import antivoland.transporeon.dataset.Dataset;
import antivoland.transporeon.model.Spot;
import antivoland.transporeon.model.segmentation.segment.Segment;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static antivoland.transporeon.model.DistanceCalculator.kmDistance;

@Disabled
@SpringBootTest
public class SegmentationTest {
    @Autowired
    AirportsDataset airportsDataset;

    @Test
    void test() {
        long start = System.currentTimeMillis();
        final AtomicInteger ops = new AtomicInteger();
        final AtomicInteger roads = new AtomicInteger();
        Segmentation segmentation = new Segmentation();
        spots().forEach(spot -> segmentation.segmentFor(spot.lat, spot.lon).spots.add(spot));
        segmentation.segments().forEach(segment -> {
            Collection<Segment> environment = segment.environment(100);
            for (int srcIdx = 0; srcIdx < segment.spots.size(); ++srcIdx) {
                Spot src = segment.spots.get(srcIdx);
                for (int dstIdx = srcIdx + 1; dstIdx < segment.spots.size(); ++dstIdx) {
                    Spot dst = segment.spots.get(dstIdx);
                    if (kmDistance(src, dst) < 100) {
                        roads.addAndGet(2);
                    }
                    ops.incrementAndGet();
                }
                environment.forEach(s->{
                    for (int dstIdx = 0; dstIdx < s.spots.size(); ++dstIdx) {
                        Spot dst = s.spots.get(dstIdx);
                        if (kmDistance(src, dst) < 100) {
                            roads.incrementAndGet();
                        }
                        ops.incrementAndGet();
                    }
                });
            }
        });
        System.out.printf("ops=%s, roads=%s, duration=%sms%n", ops, roads, System.currentTimeMillis() - start);
    }

    Stream<Spot> spots() {
        return airportsDataset
                .read()
                .map(Dataset.Airport::spot)
                .filter(spot -> !spot.codes.isEmpty());
    }
}