package antivoland.transporeon.dataset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.stream.Stream;

@Component
public class AirportsDataset implements Dataset<Airport> {
    @Value("${datasets.airports}")
    Path dataset;

    @Override
    public Stream<Airport> read() {
        return CsvReader.read(dataset).map(Airport::map);
    }
}