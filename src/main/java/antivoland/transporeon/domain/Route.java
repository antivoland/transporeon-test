package antivoland.transporeon.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;

@Builder
@EqualsAndHashCode
public class Route {
    public final Code srcAirportCode;
    public final Code dstAirportCode;
    public final boolean direct;
}