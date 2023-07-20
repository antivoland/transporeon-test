package antivoland.transporeon;

import antivoland.transporeon.exception.RouteNotFoundException;
import antivoland.transporeon.model.Code;
import antivoland.transporeon.model.Spot;
import antivoland.transporeon.model.World;
import antivoland.transporeon.model.route.Move;
import antivoland.transporeon.model.route.Route;
import antivoland.transporeon.model.route.Stop;
import org.jheaps.AddressableHeap;
import org.jheaps.tree.FibonacciHeap;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
class Router {
    private static final int MAX_NUMBER_OF_FLIGHTS = 4;

    private final World world;

    public Router(World world) {
        this.world = world;
    }

    Route findShortestRoute(Code srcCode, Code dstCode) {
        if (srcCode == null) throw new IllegalArgumentException("SRC code is missing");
        if (dstCode == null) throw new IllegalArgumentException("DST code is missing");
        Spot src = world.spot(srcCode);
        Spot dst = world.spot(dstCode);
        Route shortestRoute = findShortestRoute(src, dst);
        if (shortestRoute == null) throw new RouteNotFoundException(srcCode, dstCode);
        return shortestRoute;
    }

    private Route findShortestRoute(Spot src, Spot dst) {
        AddressableHeap<Double, Route> heap = new FibonacciHeap<>();
        Map<Stop, AddressableHeap.Handle<Double, Route>> seen = new HashMap<>();
        seen.put(Stop.first(src.id), heap.insert(0d, new Route(Stop.first(src.id))));

        AddressableHeap.Handle<Double, Route> min;
        while ((min = heap.deleteMin()) != null) {
            Route minRoute = min.getValue();
            Stop minStop = minRoute.lastStop();
            if (minStop.spotId == dst.id) return minRoute;
            for (Move move : world.outgoingMoves(minStop.spotId)) {
                if (!minRoute.canMove(move)) continue;
                Route route = minRoute.move(move);
                Stop stop = route.lastStop();
                if (route.numberOfFlights() > MAX_NUMBER_OF_FLIGHTS) continue;
                AddressableHeap.Handle<Double, Route> stopHandle = seen.get(stop);
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