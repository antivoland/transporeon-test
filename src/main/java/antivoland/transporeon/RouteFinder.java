package antivoland.transporeon;

import antivoland.transporeon.model.Spot;
import antivoland.transporeon.model.graph.Edge;
import antivoland.transporeon.model.graph.EdgeType;
import antivoland.transporeon.model.graph.Node;
import antivoland.transporeon.model.graph.NodeType;
import antivoland.transporeon.model.route.Move;
import antivoland.transporeon.model.route.MoveType;
import antivoland.transporeon.model.route.Stop;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.ValueGraph;
import org.jheaps.AddressableHeap;
import org.jheaps.AddressableHeap.Handle;
import org.jheaps.tree.FibonacciHeap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
class RouteFinder {
    private static final int MAX_STOPS = 3;

    private final List<Spot> spots;
    private final ValueGraph<Integer, Edge> routes;

    RouteFinder(List<Spot> spots, ValueGraph<Integer, Edge> routes) {
        this.spots = spots;
        this.routes = routes;
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
    Route findShortest(Spot src, Spot dst) {
        AddressableHeap<Double, Route> heap = new FibonacciHeap<>();
        Map<Node, Handle<Double, Route>> seen = new HashMap<>();
        Node srcNode = new Node(NodeType.SOURCE, src.id);
        seen.put(srcNode, heap.insert(0d, new Route(new Stop(src))));

        Handle<Double, Route> min;
        while ((min = heap.deleteMin()) != null /* todo: and min is dst */) {
            int id = min.getValue().lastStop().spot.id;
            for (EndpointPair<Integer> edge : routes.incidentEdges(id)) {
                if (edge.source() == id) {
                    Edge val = routes.edgeValue(edge).orElseThrow();
                    Node node = val.type == EdgeType.AIR
                            ? new Node(NodeType.ENTERED_BY_AIR, edge.target())
                            : new Node(NodeType.ENTERED_BY_GROUND, edge.target());
                    Handle<Double, Route> handle = seen.get(node);
                    double distance = min.getValue().kmDistance + val.distance;
                    var newRoute = min.getValue().add(val.type == EdgeType.AIR
                                    ? new Move(MoveType.BY_AIR, val.distance)
                                    : new Move(MoveType.BY_GROUND, val.distance),
                            new Stop(spots.get(node.id)));
                    if (handle == null) {
                        handle = heap.insert(distance, newRoute);
                        seen.put(node, handle);
                    } else if (distance < handle.getKey()) {
                        handle.decreaseKey(distance);
                        handle.setValue(newRoute);
                    }
                }
            }
        }
        throw new UnsupportedOperationException("Not implemented yet");
    }
}