package antivoland.transporeon.model.route;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class Move {
    public final MoveType type;
    public final double kmDistance;

    public static Move byAir(double kmDistance) {
        return new Move(MoveType.BY_AIR, kmDistance);
    }

    public static Move byGround(double kmDistance) {
        return new Move(MoveType.BY_GROUND, kmDistance);
    }
}