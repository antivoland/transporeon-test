package antivoland.transporeon;

import antivoland.transporeon.dataset.AirportsDataset;
import antivoland.transporeon.dataset.RoutesDataset;
import antivoland.transporeon.domain.Airport;
import antivoland.transporeon.domain.code.Code;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@SuppressWarnings("UnstableApiUsage")
class Router {
    private final Map<Integer, Airport> airports = new HashMap<>();
    private final MutableValueGraph<Integer, Double> routes = ValueGraphBuilder.directed().allowsSelfLoops(false).build();
    private final Map<Code, Integer> codeMapper = new HashMap<>();

    @Autowired
    Router(AirportsDataset airportsDataset, RoutesDataset routesDataset) {
        final AtomicInteger nextId = new AtomicInteger(0);
        airportsDataset
                .read()
                .filter(airport -> airport.iataCode != null || airport.icaoCode != null)
                .forEach(airport -> {
                    int id = nextId.incrementAndGet();
                    airports.put(id, airport);
                    if (airport.iataCode != null) {
                        codeMapper.put(airport.iataCode, id);
                    }
                    if (airport.icaoCode != null) {
                        codeMapper.put(airport.icaoCode, id);
                    }
                    routes.addNode(id);
                });

        routesDataset
                .read()
                .filter(route -> route.direct
                        && route.srcAirportCode != null
                        && codeMapper.containsKey(route.srcAirportCode)
                        && route.dstAirportCode != null
                        && codeMapper.containsKey(route.dstAirportCode))
                .forEach(route -> {
                    int srcAirportId = codeMapper.get(route.srcAirportCode);
                    int dstAirportId = codeMapper.get(route.dstAirportCode);
                    double distance = 0; // TODO: count the distance
                    routes.putEdgeValue(srcAirportId, dstAirportId, distance);
                });
    }

    void route() { // TODO: so what should be the signature?
        throw new UnsupportedOperationException("Not implemented yet");
    }
}