package antivoland.transporeon.dataset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.stream.Stream;

import static antivoland.transporeon.model.Code.code;

@Component
public class RoutesDataset implements Dataset<Dataset.Route> {
    private static final int SRC_AIRPORT_CODE = 2;
    private static final int DST_AIRPORT_CODE = 4;
    private static final int STOPS = 7;

    @Value("${datasets.routes}")
    Path dataset;

    @Override
    public Stream<Route> read() {
        return CsvReader.read(dataset).map(row -> Route
                .builder()
                .srcAirportCode(code(row[SRC_AIRPORT_CODE]))
                .dstAirportCode(code(row[DST_AIRPORT_CODE]))
                .direct("0".equals(row[STOPS]))
                .build());
    }
}