package antivoland.transporeon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
class Routes {
    private final Map<Integer, Set<Integer>> routes = new HashMap<>();

    @Autowired
    Routes(@Value("${datasets.routes}") Path dataset) {
        CsvReader.read(dataset).map(Route::map).forEach(route -> {
            var dstAirportIds = routes.get(route.srcAirportId);
            if (dstAirportIds == null) {
                dstAirportIds = new HashSet<>();
                routes.put(route.srcAirportId, dstAirportIds);
            }
            dstAirportIds.add(route.dstAirportId);
        });
    }
}