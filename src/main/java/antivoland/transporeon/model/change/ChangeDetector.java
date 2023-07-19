package antivoland.transporeon.model.change;

import antivoland.transporeon.model.Spot;

import java.util.Collection;

public interface ChangeDetector {
    Collection<Change> detect(Collection<Spot> spots, double kmMaxDistance);
}