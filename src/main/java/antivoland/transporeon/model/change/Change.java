package antivoland.transporeon.model.change;

import antivoland.transporeon.model.Spot;
import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class Change {
    public final Spot src;
    public final Spot dst;
    public final double kmDistance;
}