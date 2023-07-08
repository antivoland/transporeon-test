package antivoland.transporeon.model.route;

import antivoland.transporeon.model.Spot;

public class Stop implements Fragment {
    public final Spot spot;

    public Stop(Spot spot) {
        this.spot = spot;
    }

    @Override
    public double kmDistance() {
        return 0;
    }
}