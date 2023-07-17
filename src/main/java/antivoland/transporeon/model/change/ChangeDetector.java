package antivoland.transporeon.model.change;

import antivoland.transporeon.model.Spot;

import java.util.List;
import java.util.Set;

public interface ChangeDetector {
    Set<Change> detect(List<Spot> spots, double kmMaxDistance);
}