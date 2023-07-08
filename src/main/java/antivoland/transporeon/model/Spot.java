package antivoland.transporeon.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode
@AllArgsConstructor
public class Spot {
    public final List<Code> codes;
    public final double lat;
    public final double lon;
}