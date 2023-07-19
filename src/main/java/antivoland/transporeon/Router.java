package antivoland.transporeon;

import antivoland.transporeon.dataset.AirportsDataset;
import antivoland.transporeon.dataset.RoutesDataset;
import antivoland.transporeon.exception.RouteNotFoundException;
import antivoland.transporeon.exception.AirportNotFoundException;
import antivoland.transporeon.model.Code;
import antivoland.transporeon.model.Spot;
import antivoland.transporeon.model.change.SegmentationBasedChangeDetector;
import antivoland.transporeon.model.route.Move;
import antivoland.transporeon.model.route.Route;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static antivoland.transporeon.model.DistanceCalculator.kmDistance;

@Component
@SuppressWarnings("UnstableApiUsage")
class Router {
    private static final double MAX_GROUND_CROSSING_DISTANCE_KM = 100;

    private final Map<Integer, Spot> spots = new HashMap<>();
    private final Map<Code, Integer> codeMapper = new HashMap<>();
    private final RouteFinder routeFinder;

    @Autowired
    Router(AirportsDataset airportsDataset, RoutesDataset routesDataset) {
        MutableValueGraph<Integer, Move> moves = ValueGraphBuilder.directed().allowsSelfLoops(false).build();

        AtomicInteger id = new AtomicInteger(0);
        airportsDataset
                .read()
                .map(airport -> airport.spot(id.incrementAndGet()))
                .filter(spot -> !spot.codes.isEmpty())
                .forEach(spot -> {
                    spots.put(spot.id, spot);
                    moves.addNode(spot.id);
                    spot.codes.forEach(code -> codeMapper.put(code, spot.id));
                });

        new SegmentationBasedChangeDetector()
                .detect(spots.values(), MAX_GROUND_CROSSING_DISTANCE_KM)
                .forEach(change -> {
                    Move move = Move.byGround(change.kmDistance);
                    moves.putEdgeValue(change.src.id, change.dst.id, move);
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
                    Spot src = spots.get(codeMapper.get(route.srcAirportCode));
                    Spot dst = spots.get(codeMapper.get(route.dstAirportCode));
                    Move move = Move.byAir(kmDistance(src, dst));
                    moves.putEdgeValue(src.id, dst.id, move);
                });

        routeFinder = new RouteFinder(spots, moves);
    }

    Route findShortestRoute(Code srcAirportCode, Code dstAirportCode) {
        if (srcAirportCode == null) throw new IllegalArgumentException("SRC airport code is missing");
        if (dstAirportCode == null) throw new IllegalArgumentException("DST airport code is missing");
        Integer srcId = codeMapper.get(srcAirportCode);
        if (srcId == null) throw new AirportNotFoundException(srcAirportCode);
        Integer dstId = codeMapper.get(dstAirportCode);
        if (dstId == null) throw new AirportNotFoundException(dstAirportCode);
        Route shortestRoute = routeFinder.findShortestRoute(spots.get(srcId), spots.get(dstId));
        if (shortestRoute == null) throw new RouteNotFoundException(srcAirportCode, dstAirportCode);
        return shortestRoute;
    }
}