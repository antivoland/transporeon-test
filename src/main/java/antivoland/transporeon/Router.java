package antivoland.transporeon;

import antivoland.transporeon.dataset.AirportsDataset;
import antivoland.transporeon.dataset.RoutesDataset;
import antivoland.transporeon.domain.Airport;
import antivoland.transporeon.domain.Code;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@SuppressWarnings("UnstableApiUsage")
class Router {
    private static final GeodeticCalculator GEODETIC_CALCULATOR = new GeodeticCalculator();
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
                        && codeMapper.containsKey(route.dstAirportCode)
                        && !route.srcAirportCode.equals(route.dstAirportCode))
                .forEach(route -> {
                    int srcAirportId = codeMapper.get(route.srcAirportCode);
                    int dstAirportId = codeMapper.get(route.dstAirportCode);
                    double kmDistance = kmDistance(srcAirportId, dstAirportId);
                    routes.putEdgeValue(srcAirportId, dstAirportId, kmDistance);
                });
    }

    void route() { // TODO: so what should be the signature?
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private double kmDistance(int srcAirportId, int dstAirportId) {
        Airport srcAirport = airports.get(srcAirportId);
        Airport dstAirport = airports.get(dstAirportId);
        GlobalCoordinates src = new GlobalCoordinates(srcAirport.lat, srcAirport.lon);
        GlobalCoordinates dst = new GlobalCoordinates(dstAirport.lat, dstAirport.lon);
        GeodeticCurve curve = GEODETIC_CALCULATOR.calculateGeodeticCurve(Ellipsoid.WGS84, src, dst);
        return curve.getEllipsoidalDistance() / 1000;
    }
}