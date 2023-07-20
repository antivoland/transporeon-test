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

    public static Stop first(int spotId) {
        return new Stop(StopType.FIRST, spotId);
    }

    public static Stop enteredBy(Move move) {
        return switch (move.type) {
            case BY_AIR -> new Stop(StopType.ENTERED_BY_AIR, move.dst.id);
            case BY_GROUND -> new Stop(StopType.ENTERED_BY_GROUND, move.dst.id);
        };
    }
}