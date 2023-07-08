package antivoland.transporeon.dataset;

import antivoland.transporeon.domain.Airport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.stream.Stream;

import static antivoland.transporeon.domain.code.Code.code;

@Component
public class AirportsDataset implements Dataset<Airport> {
    private static final int IATA_CODE = 4;
    private static final int ICAO_CODE = 5;
    private static final int LAT = 6;
    private static final int LON = 7;

    @Value("${datasets.airports}")
    Path dataset;

    @Override
    public Stream<Airport> read() {
        return CsvReader.read(dataset).map(row -> Airport
                .builder()
                .iataCode(code(row[IATA_CODE]))
                .icaoCode(code(row[ICAO_CODE]))
                .lat(Double.parseDouble(row[LAT]))
                .lon(Double.parseDouble(row[LON]))
                .build());
    }
}