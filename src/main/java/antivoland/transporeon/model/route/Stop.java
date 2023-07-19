package antivoland.transporeon.model.route;

import antivoland.transporeon.model.Spot;
import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class Stop {
    public final StopType type;
    public final Spot spot;
}