package antivoland.transporeon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Component
class Airports {
    private final Map<String, Airport> iata = new HashMap<>();
    private final Map<String, Airport> icao = new HashMap<>();

    @Autowired
    Airports(@Value("${datasets.airports}") Path dataset) {
        CsvReader.read(dataset).map(Airport::map).forEach(airport -> {
            if (airport.iata != null) iata.put(airport.iata, airport);
            if (airport.icao != null) icao.put(airport.icao, airport);
        });
    }
}