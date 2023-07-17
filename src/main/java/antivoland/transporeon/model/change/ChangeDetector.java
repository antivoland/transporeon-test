package antivoland.transporeon.model.change;

import antivoland.transporeon.model.Spot;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ChangeDetector {
    public final Set<Change> detect(List<Spot> spots, double kmMaxDistance) {
        return detect(spots, kmMaxDistance, new AtomicInteger());
    }

    abstract Set<Change> detect(List<Spot> spots, double kmMaxDistance, AtomicInteger numberOfDistanceCalculations);
}