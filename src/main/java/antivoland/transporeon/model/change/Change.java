package antivoland.transporeon.model.change;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Change {
    final int srcId;
    final int dstId;
}