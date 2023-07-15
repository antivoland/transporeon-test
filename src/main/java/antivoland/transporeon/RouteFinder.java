package antivoland.transporeon;

import antivoland.transporeon.model.Spot;
import antivoland.transporeon.model.route.Route;
import com.google.common.graph.ValueGraph;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
class RouteFinder {
    private static final int MAX_STOPS = 3;

    private final Map<Integer, Spot> spots;
    private final ValueGraph<Integer, Double> routes;

    RouteFinder(Map<Integer, Spot> spots, ValueGraph<Integer, Double> routes) {
        this.spots = spots;
        this.routes = routes;
    }

    Route findShortest(Spot src, Spot dst) {
//        Set<Integer> visited = new HashSet<>();
//        Distances distances = new Distances();
//        TreeMap<Double, Integer> c = new TreeMap<>()
//        c.
//        PriorityQueue<>
//        for (EndpointPair<Integer> edge : routes.incidentEdges(srcId)) {
//            if (edge.source() == srcId) {
//
//            }
//        }
//        routes.incidentEdges(srcId)
//
//        Spot srcSpot = spots.get(srcId);
//        Spot dstSpot = spots.get(dstId);
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private static class Distances {
        final Map<Integer, Double> values = new HashMap<>();

        double get(int id) {
            return values.getOrDefault(id, Double.POSITIVE_INFINITY);
        }

        void put(int id, double value) {
            values.put(id, value);
        }
    }
}