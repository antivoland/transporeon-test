package antivoland.transporeon.model.route;

import antivoland.transporeon.model.Spot;
import antivoland.transporeon.model.change.Change;
import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class Move {
    public final MoveType type;
    public final Spot src;
    public final Spot dst;
    public final double kmDistance;

    public static Move byAir(Spot src, Spot dst, double kmDistance) {
        return new Move(MoveType.BY_AIR, src, dst, kmDistance);
    }

    public static Move byGround(Change change) {
        return byGround(change.src, change.dst, change.kmDistance);
    }

    public static Move byGround(Spot src, Spot dst, double kmDistance) {
        return new Move(MoveType.BY_GROUND, src, dst, kmDistance);
    }
}