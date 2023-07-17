package antivoland.transporeon;

import antivoland.transporeon.dataset.AirportsDataset;
import antivoland.transporeon.dataset.Dataset;
import antivoland.transporeon.dataset.RoutesDataset;
import antivoland.transporeon.model.Code;
import antivoland.transporeon.model.Spot;
import antivoland.transporeon.model.route.Route;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static antivoland.transporeon.model.DistanceCalculator.kmDistance;
import static java.lang.String.format;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Component
@SuppressWarnings("UnstableApiUsage")
class Router {
    private static final double MAX_GROUND_CROSSING_DISTANCE_KM = 100;

    private final List<Spot> spots = new ArrayList<>();
    private final Map<Code, Integer> codeMapper = new HashMap<>();
    private final RouteFinder routeFinder;

    @Autowired
    Router(AirportsDataset airportsDataset, RoutesDataset routesDataset) {
        MutableValueGraph<Integer, Double> routes = ValueGraphBuilder.directed().allowsSelfLoops(false).build();

        airportsDataset
                .read()
                .map(Dataset.Airport::spot)
                .filter(spot -> !spot.codes.isEmpty())
                .forEach(spot -> {
                    int id = spots.size();
                    spots.add(spot);
                    routes.addNode(id);
                    spot.codes.forEach(code -> codeMapper.put(code, id));
                });

        long start = System.currentTimeMillis();
        final AtomicInteger roads = new AtomicInteger();
        final AtomicInteger ops = new AtomicInteger();
//        for (int srcId = 0; srcId < spots.size(); ++srcId) {
//            for (int dstId = srcId + 1; dstId < spots.size(); ++dstId) {
//                if (kmDistance(spots.get(srcId), spots.get(dstId)) < MAX_GROUND_CROSSING_DISTANCE_KM) {
//                    roads.incrementAndGet();
//                }
//                ops.incrementAndGet();
//            }
//        }
        IntStream.range(0, spots.size()).parallel().forEach(srcId -> {
            for (int dstId = srcId + 1; dstId < spots.size(); ++dstId) {
                if (kmDistance(spots.get(srcId), spots.get(dstId)) < MAX_GROUND_CROSSING_DISTANCE_KM) {
                    roads.addAndGet(2);
                }
                ops.incrementAndGet();
            }
        });
        System.out.printf("ops=%s, roads=%s, duration=%sms%n", ops, roads, System.currentTimeMillis() - start);
        // non-parallel: ops=29618056, roads=18373, duration=42000ms
        // parallel: ops=29618056, roads=18373, duration=5193ms

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
                    routes.putEdgeValue(srcId, dstId, kmDistance(spots.get(srcId), spots.get(dstId)));
                });

        routeFinder = new RouteFinder(spots, routes);

        var minLon = spots.stream().map(s -> s.lon).min(Double::compareTo);
        var maxLon = spots.stream().map(s -> s.lon).max(Double::compareTo);
        var minLat = spots.stream().map(s -> s.lat).min(Double::compareTo);
        var maxLat = spots.stream().map(s -> s.lat).max(Double::compareTo);

        var groups = spots
                .stream()
                .collect(groupingBy(Group::new, counting()));

        var groupsByLat = spots
                .stream()
                .collect(groupingBy(g -> (int) g.lat, TreeMap::new, counting()));
        var sumNorth75 = groupsByLat.entrySet().stream().filter(s -> s.getKey() >= 75).mapToLong(s -> s.getValue()).sum();
        var sumBetween = groupsByLat.entrySet().stream().filter(s -> s.getKey() > -75 && s.getKey() < 75).mapToLong(s -> s.getValue()).sum();
        var sumSouth75 = groupsByLat.entrySet().stream().filter(s -> s.getKey() <= -75).mapToLong(s -> s.getValue()).sum();
        System.out.printf("");

    }

    class Group {
        int intLat;
        int intLon;

        Group(Spot spot) {
            intLat = (int) spot.lat;
            intLon = (int) spot.lon;
        }

        @Override
        public String toString() {
            return "Group{" +
                    "intLat=" + intLat +
                    ", intLon=" + intLon +
                    '}';
        }
    }

    Route findShortestRoute(Code srcCode, Code dstCode) {
        if (srcCode == null) throw new IllegalArgumentException("SRC airport code is missing");
        if (dstCode == null) throw new IllegalArgumentException("DST airport code is missing");
        Integer srcId = codeMapper.get(srcCode);
        if (srcId == null) throw new SpotNotFoundException(srcCode);
        Integer dstId = codeMapper.get(dstCode);
        if (dstId == null) throw new SpotNotFoundException(dstCode);
        return routeFinder.findShortest(spots.get(srcId), spots.get(dstId));
    }
}