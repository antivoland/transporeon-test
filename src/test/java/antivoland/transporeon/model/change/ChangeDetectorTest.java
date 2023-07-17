package antivoland.transporeon.model.change;

import antivoland.transporeon.dataset.AirportsDataset;
import antivoland.transporeon.dataset.Dataset;
import antivoland.transporeon.model.Spot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        Set<Change> bruteForceChanges = bruteForce.detect(spots, MAX_DISTANCE_KM);
        System.out.printf("Brute force: changes=%s, duration=%sms%n", bruteForceChanges.size(), System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        SegmentationBasedChangeDetector segmentationBased = new SegmentationBasedChangeDetector();
        Set<Change> segmentationBasedChanges = segmentationBased.detect(spots, MAX_DISTANCE_KM);
        System.out.printf("Segmentation based: changes=%s, duration=%sms%n", segmentationBasedChanges.size(), System.currentTimeMillis() - start);

        segmentationBasedChanges
                .stream()
                .filter(change -> !bruteForceChanges.contains(change))
                .forEach(change -> {
                    System.out.printf("Brute force: missing change (%s)=>(%s)%n",
                            airports.get(change.srcId), airports.get(change.dstId));
                });

        bruteForceChanges
                .stream()
                .filter(change -> !segmentationBasedChanges.contains(change))
                .forEach(change -> {
                    System.out.printf("Segmentation based: missing change (%s)=>(%s)%n",
                            airports.get(change.srcId), airports.get(change.dstId));
                });

        // todo: few corner spots are not detected as changes
    }

    List<Dataset.Airport> airports() {
        return airportsDataset.read().filter(airport -> !airport.codes().isEmpty()).toList();
    }
}