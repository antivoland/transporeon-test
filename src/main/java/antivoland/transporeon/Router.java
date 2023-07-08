package antivoland.transporeon;

import antivoland.transporeon.dataset.AirportsDataset;
import antivoland.transporeon.dataset.Dataset;
import antivoland.transporeon.dataset.RoutesDataset;
import antivoland.transporeon.model.Code;
import antivoland.transporeon.model.Spot;
import antivoland.transporeon.model.route.Route;
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
    private static final int MAX_STOPS = 3;
    private static final GeodeticCalculator GEODETIC_CALCULATOR = new GeodeticCalculator();

    private final Map<Integer, Spot> spots = new HashMap<>();
    private final MutableValueGraph<Integer, Double> routes = ValueGraphBuilder.directed().allowsSelfLoops(false).build();
    private final Map<Code, Integer> codeMapper = new HashMap<>();

    @Autowired
    Router(AirportsDataset airportsDataset, RoutesDataset routesDataset) {
        final AtomicInteger nextId = new AtomicInteger(0);
        airportsDataset
                .read()
                .map(Dataset.Airport::spot)
                .filter(spot -> !spot.codes.isEmpty())
                .forEach(spot -> {
                    int id = nextId.incrementAndGet();
                    spots.put(id, spot);
                    routes.addNode(id);
                    spot.codes.forEach(code -> codeMapper.put(code, id));
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
                    int srcId = codeMapper.get(route.srcAirportCode);
                    int dstId = codeMapper.get(route.dstAirportCode);
                    routes.putEdgeValue(srcId, dstId, kmDistance(srcId, dstId));
                });
        System.out.println();
    }

    Route route(Code srcCode, Code dstCode) {
        if (srcCode == null) {
            throw new IllegalArgumentException("SRC airport code is missing");
        }
        if (dstCode == null) {
            throw new IllegalArgumentException("DST airport code is missing");
        }
        if (!codeMapper.containsKey(srcCode)) {
            throw new SpotNotFoundException(srcCode);
        }
        if (!codeMapper.containsKey(dstCode)) {
            throw new SpotNotFoundException(dstCode);
        }
        int srcId = codeMapper.get(srcCode);
        int dstId = codeMapper.get(dstCode);
        if (srcCode == dstCode) {
            // TODO: return route (src)->(dst)
        }
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private double kmDistance(int srcAirportId, int dstAirportId) {
        Spot srcSpot = spots.get(srcAirportId);
        Spot dstSpot = spots.get(dstAirportId);
        GlobalCoordinates src = new GlobalCoordinates(srcSpot.lat, srcSpot.lon);
        GlobalCoordinates dst = new GlobalCoordinates(dstSpot.lat, dstSpot.lon);
        GeodeticCurve curve = GEODETIC_CALCULATOR.calculateGeodeticCurve(Ellipsoid.WGS84, src, dst);
        return curve.getEllipsoidalDistance() / 1000;
    }
}