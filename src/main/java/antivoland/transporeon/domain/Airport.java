package antivoland.transporeon.domain;

import antivoland.transporeon.domain.code.Code;
import lombok.Builder;
import lombok.EqualsAndHashCode;

@Builder
@EqualsAndHashCode
public class Airport {
    public final Code iataCode;
    public final Code icaoCode;
    public final double lat;
    public final double lon;
}