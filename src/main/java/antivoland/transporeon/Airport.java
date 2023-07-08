package antivoland.transporeon;

import lombok.Builder;
import lombok.EqualsAndHashCode;

@Builder
@EqualsAndHashCode
class Airport {
    private static final int ID = 0;
    private static final int IATA = 4;
    private static final int ICAO = 5;
    private static final int LAT = 6;
    private static final int LON = 7;

    int id;
    String iata;
    String icao;
    double lat;
    double lon;

    static Airport map(String[] row) {
        return Airport
                .builder()
                .id(Integer.parseInt(row[ID]))
                .iata(row[IATA])
                .icao(row[ICAO])
                .lat(Double.parseDouble(row[LAT]))
                .lon(Double.parseDouble(row[LON]))
                .build();
    }
}