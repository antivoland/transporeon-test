package antivoland.transporeon.model.change;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Change {
    public final int srcId;
    public final int dstId;
}