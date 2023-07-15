package antivoland.transporeon.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Spot {
    public final List<Code> codes;
    public final double lat;
    public final double lon;
}