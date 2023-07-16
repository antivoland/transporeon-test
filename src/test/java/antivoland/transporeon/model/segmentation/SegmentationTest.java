package antivoland.transporeon.model.segmentation;

import antivoland.transporeon.dataset.AirportsDataset;
import antivoland.transporeon.dataset.Dataset;
import antivoland.transporeon.model.Spot;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

@Disabled
@SpringBootTest
public class SegmentationTest {
    @Autowired
    AirportsDataset airportsDataset;

    @Test
    void test() {
        Segmentation segmentation = new Segmentation();
        spots().forEach(spot -> segmentation.segmentFor(spot.lat, spot.lon).spots.add(spot));
        segmentation.segments().forEach(segment -> {
            var env = segmentation.segmentEnvironment(segment, 100);
            System.out.println();
        });
        System.out.println(segmentation);
    }

    Stream<Spot> spots() {
        return airportsDataset
                .read()
                .map(Dataset.Airport::spot)
                .filter(spot -> !spot.codes.isEmpty());
    }
}