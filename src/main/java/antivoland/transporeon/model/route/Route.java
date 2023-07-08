package antivoland.transporeon.model.route;

public class Route {
    public final Fragment[] fragments;
    public final double kmDistance;

    public Route(Fragment... fragments) {
        this.fragments = fragments;
        double kmDistance = 0;
        for (Fragment fragment : fragments) {
            kmDistance += fragment.kmDistance();
        }
        this.kmDistance = kmDistance;
    }

    public Route add(Fragment fragment) {
        Fragment[] newFragments = new Fragment[fragments.length + 1];
        System.arraycopy(fragments, 0, newFragments, 0, fragments.length);
        newFragments[fragments.length] = fragment;
        return new Route(newFragments);
    }
}