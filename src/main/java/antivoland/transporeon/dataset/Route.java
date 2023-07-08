package antivoland.transporeon.dataset;

import antivoland.transporeon.code.Code;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import static antivoland.transporeon.code.Code.code;

@Builder
@EqualsAndHashCode
public class Route {
    private static final int SRC_AIRPORT_ID = 3;
    private static final int DST_AIRPORT_ID = 5;
    private static final int STOPS = 7;

    public Code srcAirportCode;
    public Code dstAirportCode;
    public boolean direct;

    static Route map(String[] row) {
        return Route
                .builder()
                .srcAirportCode(code(row[SRC_AIRPORT_ID]))
                .dstAirportCode(code(row[DST_AIRPORT_ID]))
                .direct("0".equals(row[STOPS]))
                .build();
    }
}