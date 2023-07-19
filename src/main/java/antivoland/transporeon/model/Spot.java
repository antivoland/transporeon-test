package antivoland.transporeon.model;

import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.Set;

@ToString
@AllArgsConstructor
public class Spot {
    public final int id;
    public final double lat;
    public final double lon;
    public final Set<Code> codes;
}