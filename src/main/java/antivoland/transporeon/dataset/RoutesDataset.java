package antivoland.transporeon.dataset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.stream.Stream;

@Component
public class RoutesDataset implements Dataset<Route> {
    @Value("${datasets.routes}")
    Path dataset;

    @Override
    public Stream<Route> read() {
        return CsvReader.read(dataset).map(Route::map);
    }
}