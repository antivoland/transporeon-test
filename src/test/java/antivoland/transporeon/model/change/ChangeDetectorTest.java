package antivoland.transporeon.model.change;

import antivoland.transporeon.dataset.AirportsDataset;
import antivoland.transporeon.dataset.Dataset;
import antivoland.transporeon.model.Spot;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Disabled
@SpringBootTest
public class ChangeDetectorTest {
    private static final double MAX_DISTANCE_KM = 100;

    @Autowired
    AirportsDataset airportsDataset;

    @Test
    void test() {
        List<Dataset.Airport> airports = airports();
        List<Spot> spots = new ArrayList<>();
        for (int idx = 0; idx < airports.size(); ++idx) {
            spots.add(new Spot(idx, airports.get(idx).lat, airports.get(idx).lon));
        }
        System.out.printf("Dataset: size=%s%n", spots.size());

        long start = System.currentTimeMillis();
        BruteForceChangeDetector bruteForce = new BruteForceChangeDetector();
        AtomicInteger bruteForceNumberOfDistanceCalculations = new AtomicInteger(0);
        Set<Change> bruteForceChanges = bruteForce.detect(spots, MAX_DISTANCE_KM, bruteForceNumberOfDistanceCalculations);
        System.out.printf("Brute force: numberOfDistanceCalculations=%s, changes=%s, duration=%sms%n",
                bruteForceNumberOfDistanceCalculations, bruteForceChanges.size(), System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        SegmentationBasedChangeDetector segmentationBased = new SegmentationBasedChangeDetector();
        AtomicInteger segmentationBasedNumberOfDistanceCalculations = new AtomicInteger(0);
        Set<Change> segmentationBasedChanges = segmentationBased.detect(spots, MAX_DISTANCE_KM, segmentationBasedNumberOfDistanceCalculations);
        System.out.printf("Segmentation based: numberOfDistanceCalculations=%s, changes=%s, duration=%sms%n",
                segmentationBasedNumberOfDistanceCalculations, segmentationBasedChanges.size(), System.currentTimeMillis() - start);

        bruteForceChanges
                .stream()
                .filter(change -> !segmentationBasedChanges.contains(change))
                .forEach(change -> {
                    System.out.printf("Segmentation based: missing change (%s)=>(%s)%n",
                            airports.get(change.srcId), airports.get(change.dstId));
                });

        segmentationBasedChanges
                .stream()
                .filter(change -> !bruteForceChanges.contains(change))
                .forEach(change -> {
                    System.out.printf("Brute force: missing change (%s)=>(%s)%n",
                            airports.get(change.srcId), airports.get(change.dstId));
                });
    }

    List<Dataset.Airport> airports() {
        return airportsDataset.read().filter(airport -> !airport.codes().isEmpty()).toList();
    }
}