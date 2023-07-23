package antivoland.transporeon.model.change;

import antivoland.transporeon.model.Spot;
import antivoland.transporeon.model.change.ChangeDetectorTest.ChangeId;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static antivoland.transporeon.model.CodeTest.codes;
import static antivoland.transporeon.model.change.ChangeDetectorTest.MAX_DISTANCE_KM;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

class SegmentationBasedChangeDetectorTest {
    /*
     This test is based on error output from the main change detector test:
     - Brute force only change: Change(
       src=Spot(id=4543, lat=-16.466699600219727, lon=179.33999633789062, codes=[IATA(LBS), ICAO(NFNL)]),
       dst=Spot(id=4544, lat=-16.6905994415, lon=-179.876998901, codes=[IATA(TVU), ICAO(NFNM)]), kmDistance=87.15900394830591)
     - Brute force only change: Change(
       src=Spot(id=4544, lat=-16.6905994415, lon=-179.876998901, codes=[IATA(TVU), ICAO(NFNM)]),
       dst=Spot(id=4547, lat=-16.8027992249, lon=179.341003418, codes=[IATA(SVU), ICAO(NFNS)]), kmDistance=84.30209256166664)
     - Brute force only change: Change(
       src=Spot(id=4544, lat=-16.6905994415, lon=-179.876998901, codes=[IATA(TVU), ICAO(NFNM)]),
       dst=Spot(id=4543, lat=-16.466699600219727, lon=179.33999633789062, codes=[IATA(LBS), ICAO(NFNL)]), kmDistance=87.15900394830594)
     - Brute force only change: Change(
       src=Spot(id=4547, lat=-16.8027992249, lon=179.341003418, codes=[IATA(SVU), ICAO(NFNS)]),
       dst=Spot(id=4544, lat=-16.6905994415, lon=-179.876998901, codes=[IATA(TVU), ICAO(NFNM)]), kmDistance=84.30209256166664)
     */
    @Test
    void test() {
        Collection<Spot> spots = List.of(
                new Spot(4543, -16.466699600219727, 179.33999633789062, codes("LBS", "NFNL")),
                new Spot(4544, -16.6905994415, -179.876998901, codes("TVU", "NFNM")),
                new Spot(4547, -16.8027992249, 179.341003418, codes("SVU", "NFNS")));
        ChangeDetector detector = new SegmentationBasedChangeDetector();
        Collection<Change> changes = detector.detect(spots, MAX_DISTANCE_KM);
        assertThat(changes).hasSizeGreaterThan(0);
        changes.forEach(change -> assertThat(change.kmDistance).isLessThan(MAX_DISTANCE_KM));
        Set<ChangeId> changeIds = changes.stream().map(ChangeId::new).collect(toSet());
        assertThat(changeIds).isEqualTo(Set.of(
                new ChangeId(4543, 4544),
                new ChangeId(4543, 4547),
                new ChangeId(4544, 4543),
                new ChangeId(4544, 4547),
                new ChangeId(4547, 4543),
                new ChangeId(4547, 4544)));
    }
}