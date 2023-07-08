package antivoland.transporeon.dataset;

import antivoland.transporeon.code.Code;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import static antivoland.transporeon.code.Code.code;

@Builder
@EqualsAndHashCode
public class Airport {
    private static final int IATA_CODE = 4;
    private static final int ICAO_CODE = 5;
    private static final int LAT = 6;
    private static final int LON = 7;

    public Code iataCode;
    public Code icaoCode;
    public double lat;
    public double lon;

    static Airport map(String[] row) {
        return Airport
                .builder()
                .iataCode(code(row[IATA_CODE]))
                .icaoCode(code(row[ICAO_CODE]))
                .lat(Double.parseDouble(row[LAT]))
                .lon(Double.parseDouble(row[LON]))
                .build();
    }
}