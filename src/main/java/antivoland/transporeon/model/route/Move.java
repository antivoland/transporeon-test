package antivoland.transporeon.model.route;

public class Move implements Fragment {
    public final MoveType type;
    public final double kmDistance;

    public Move(MoveType type, double kmDistance) {
        this.type = type;
        this.kmDistance = kmDistance;
    }

    @Override
    public double kmDistance() {
        return kmDistance;
    }

    @Override
    public boolean isFlight() {
        return type == MoveType.AIR;
    }
}