package antivoland.transporeon.model;

import antivoland.transporeon.exception.RouteNotFoundException;
import antivoland.transporeon.model.route.Move;
import antivoland.transporeon.model.route.Route;
import antivoland.transporeon.model.route.Stop;
import org.jheaps.AddressableHeap;
import org.jheaps.AddressableHeap.Handle;
import org.jheaps.tree.FibonacciHeap;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;

@Component
public class Router {
    private static final int MAX_NUMBER_OF_FLIGHTS = 4;

    private final World world;

    public Router(World world) {
        this.world = world;
    }

    public Route findShortestRoute(String srcCode, String dstCode) {
        return findShortestRoute(srcCode, dstCode, true);
    }

    public Route findShortestRoute(String srcCode, String dstCode, boolean limited) {
        return findShortestRoute(
                Optional.ofNullable(Code.code(srcCode)).orElseThrow(() ->
                        new IllegalArgumentException(format("SRC code %s is invalid", srcCode))),
                Optional.ofNullable(Code.code(dstCode)).orElseThrow(() ->
                        new IllegalArgumentException(format("DST code %s is invalid", dstCode))),
                limited);
    }

    private Route findShortestRoute(Code srcCode, Code dstCode, boolean limited) {
        if (srcCode == null) throw new IllegalArgumentException("SRC code is missing");
        if (dstCode == null) throw new IllegalArgumentException("DST code is missing");
        Spot src = world.spot(srcCode);
        Spot dst = world.spot(dstCode);
        Route shortestRoute = findShortestRoute(src, dst, limited);
        if (shortestRoute == null) throw new RouteNotFoundException(srcCode, dstCode);
        return shortestRoute;
    }

    private Route findShortestRoute(Spot src, Spot dst, boolean limited) {
        AddressableHeap<Double, Route> heap = new FibonacciHeap<>();
        Map<Stop, Handle<Double, Route>> seen = new HashMap<>();
        seen.put(Stop.first(src.id), heap.insert(0d, new Route(src.id)));

        while (!heap.isEmpty()) {
            Handle<Double, Route> min = heap.deleteMin();
            Route minRoute = min.getValue();
            Stop minStop = minRoute.lastStop;
            if (minStop.spotId == dst.id) return minRoute;
            for (Move move : world.outgoingMoves(minStop.spotId)) {
                if (!minRoute.canMove(move)) continue;
                Route route = minRoute.move(move);
                Stop stop = route.lastStop;
                if (limited && route.numberOfFlights() > MAX_NUMBER_OF_FLIGHTS) continue;
                Handle<Double, Route> stopHandle = seen.get(stop);
                if (stopHandle == null) {
                    stopHandle = heap.insert(route.kmDistance, route);
                    seen.put(stop, stopHandle);
                } else if (route.kmDistance < stopHandle.getKey()) {
                    stopHandle.decreaseKey(route.kmDistance);
                    stopHandle.setValue(route);
                }
            }
        }
        return null;
    }
}