package antivoland.transporeon;

import antivoland.transporeon.model.Spot;
import antivoland.transporeon.model.route.Move;
import antivoland.transporeon.model.route.Route;
import antivoland.transporeon.model.route.Stop;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.ValueGraph;
import org.jheaps.AddressableHeap;
import org.jheaps.AddressableHeap.Handle;
import org.jheaps.tree.FibonacciHeap;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
class RouteFinder {
    private static final int MAX_NUMBER_OF_FLIGHTS = 4;

    private final Map<Integer, Spot> spots; // todo: remove
    private final ValueGraph<Integer, Move> moves;

    RouteFinder(Map<Integer, Spot> spots, ValueGraph<Integer, Move> moves) {
        this.spots = spots;
        this.moves = moves;
    }

    /*
     function Dijkstra(Graph, source):
         dist[source] ← 0                           // Initialization

         create vertex priority queue Q

         for each vertex v in Graph.Vertices:
             if v ≠ source
                 dist[v] ← INFINITY                 // Unknown distance from source to v
                 prev[v] ← UNDEFINED                // Predecessor of v

             Q.add_with_priority(v, dist[v])


         while Q is not empty:                      // The main loop
             u ← Q.extract_min()                    // Remove and return best vertex
             for each neighbor v of u:              // Go through all v neighbors of u
                 alt ← dist[u] + Graph.Edges(u, v)
                 if alt < dist[v]:
                     dist[v] ← alt
                     prev[v] ← u
                     Q.decrease_priority(v, alt)

         return dist, prev
     */
    Route findShortestRoute(Spot src, Spot dst) {
        AddressableHeap<Double, Route> heap = new FibonacciHeap<>();
        Map<Stop, Handle<Double, Route>> seen = new HashMap<>();
        seen.put(Stop.first(src.id), heap.insert(0d, new Route(Stop.first(src.id))));

        Handle<Double, Route> min;
        while ((min = heap.deleteMin()) != null) {
            Route route = min.getValue();
            Stop stop = route.lastStop();
            if (stop.spotId == dst.id) return route;
            for (EndpointPair<Integer> edge : moves.incidentEdges(stop.spotId)) {
                if (edge.source() != stop.spotId) continue;
                Move move = moves.edgeValue(edge).orElseThrow();
                Stop nextStop = Stop.enteredBy(move.type, edge.target());
                if (!stop.canMoveTo(nextStop, move)) continue;
                var nextStopRoute = route.moveTo(nextStop, move);
                if (nextStopRoute.numberOfFlights() > MAX_NUMBER_OF_FLIGHTS) continue;
                Handle<Double, Route> nextStopHandle = seen.get(nextStop);
                if (nextStopHandle == null) {
                    nextStopHandle = heap.insert(nextStopRoute.kmDistance, nextStopRoute);
                    seen.put(nextStop, nextStopHandle);
                } else if (nextStopRoute.kmDistance < nextStopHandle.getKey()) {
                    nextStopHandle.decreaseKey(nextStopRoute.kmDistance);
                    nextStopHandle.setValue(nextStopRoute);
                }
            }
        }
        return null;
    }
}