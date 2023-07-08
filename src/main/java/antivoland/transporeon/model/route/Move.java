package antivoland.transporeon.model.route;

public class Move implements Fragment {
    public enum Type {AIR, GROUND}

    public final Type type;
    public final double kmDistance;

    public Move(Type type, double kmDistance) {
        this.type = type;
        this.kmDistance = kmDistance;
    }

    @Override
    public double kmDistance() {
        return kmDistance;
    }
}