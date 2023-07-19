package antivoland.transporeon.model;

import lombok.Builder;
import lombok.ToString;

import java.util.Set;

@Builder
@ToString
public class Spot {
    public final int id;
    public final double lat;
    public final double lon;
    public final Set<Code> codes;
}