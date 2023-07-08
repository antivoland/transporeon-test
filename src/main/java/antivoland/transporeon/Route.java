package antivoland.transporeon;

import lombok.Builder;
import lombok.EqualsAndHashCode;

@Builder
@EqualsAndHashCode
class Route {
    private static final int SRC_AIRPORT_ID = 3;
    private static final int DST_AIRPORT_ID = 5;
    private static final int STOPS = 7;

    int srcAirportId;
    int dstAirportId;
    boolean direct;

    static Route map(String[] row) {
        return Route
                .builder()
                .srcAirportId(Integer.parseInt(row[SRC_AIRPORT_ID]))
                .dstAirportId(Integer.parseInt(row[DST_AIRPORT_ID]))
                .direct("0".equals(row[STOPS]))
                .build();
    }
}