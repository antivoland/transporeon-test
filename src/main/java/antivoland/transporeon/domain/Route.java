package antivoland.transporeon.domain;

import antivoland.transporeon.domain.code.Code;
import lombok.Builder;
import lombok.EqualsAndHashCode;

@Builder
@EqualsAndHashCode
public class Route {
    public final Code srcAirportCode;
    public final Code dstAirportCode;
    public final boolean direct;
}