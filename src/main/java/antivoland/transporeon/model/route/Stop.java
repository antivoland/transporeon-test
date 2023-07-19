package antivoland.transporeon.model.route;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Stop {
    public final StopType type;
    public final int spotId;

    public boolean canMoveTo(Stop stop, Move move) {
        return stop.type != StopType.ENTERED_BY_GROUND || move.type != MoveType.BY_GROUND;
    }

    public static Stop first(int spotId) {
        return new Stop(StopType.FIRST, spotId);
    }

    public static Stop enteredBy(MoveType moveType, int spotId) {
        return switch (moveType) {
            case BY_AIR -> new Stop(StopType.ENTERED_BY_AIR, spotId);
            case BY_GROUND -> new Stop(StopType.ENTERED_BY_GROUND, spotId);
        };
    }
}