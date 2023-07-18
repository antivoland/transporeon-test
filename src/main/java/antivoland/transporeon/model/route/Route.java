package antivoland.transporeon.model.route;

public class Route {
    public final Fragment[] fragments;
    public final double kmDistance;
    public final int numberOfFlights;

    public Route(Stop src) {
        this(new Fragment[]{src});
    }

    private Route(Fragment[] fragments) {
        this.fragments = fragments;
        double kmDistance = 0;
        int numberOfFlights = 0;
        for (Fragment fragment : fragments) {
            kmDistance += fragment.kmDistance();
            if (fragment.isFlight()) ++numberOfFlights;
        }
        this.kmDistance = kmDistance;
        this.numberOfFlights = numberOfFlights;
    }

    public Stop lastStop() {
        return (Stop) fragments[fragments.length - 1];
    }

    public Route add(Move move, Stop stop) {
        Fragment[] newFragments = new Fragment[fragments.length + 2];
        System.arraycopy(fragments, 0, newFragments, 0, fragments.length);
        newFragments[fragments.length] = move;
        newFragments[fragments.length + 1] = stop;
        return new Route(newFragments);
    }
}