package antivoland.transporeon;

import antivoland.transporeon.dataset.AirportsDataset;
import antivoland.transporeon.dataset.RoutesDataset;
import antivoland.transporeon.model.Code;
import antivoland.transporeon.model.Spot;
import antivoland.transporeon.model.change.SegmentationBasedChangeDetector;
import antivoland.transporeon.model.route.Move;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import static antivoland.transporeon.model.DistanceCalculator.kmDistance;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

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
                .map(airport -> Spot
                        .builder()
                        .id(id.incrementAndGet())
                        .lat(airport.lat)
                        .lon(airport.lon)
                        .codes(airport.codes())
                        .build())
                .filter(spot -> !spot.codes.isEmpty())
                .forEach(spot -> {
                    spots.put(spot.id, spot);
                    moves.addNode(spot.id);
                    spot.codes.forEach(code -> codeMapper.put(code, spot.id));
                });

        new SegmentationBasedChangeDetector()
                .detect(spots.values(), MAX_GROUND_CROSSING_DISTANCE_KM)
                .forEach(change -> {
                    Spot src = spots.get(change.srcId);
                    Spot dst = spots.get(change.dstId);
                    Move move = Move.byGround(kmDistance(src, dst));
                    moves.putEdgeValue(src.id, dst.id, move);
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
                    Spot src = spots.get(srcId);
                    Spot dst = spots.get(dstId);
                    Edge val = new Edge(EdgeType.AIR, kmDistance(src, dst));
                    moves.putEdgeValue(srcId, dstId, val);
                });

        routeFinder = new RouteFinder(spots, moves);

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