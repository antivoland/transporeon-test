package antivoland.transporeon.model;

import antivoland.transporeon.dataset.AirportDataset;
import antivoland.transporeon.dataset.RouteDataset;
import antivoland.transporeon.exception.SpotNotFoundException;
import antivoland.transporeon.model.change.SegmentationBasedChangeDetector;
import antivoland.transporeon.model.route.Move;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static antivoland.transporeon.model.DistanceCalculator.kmDistance;

@Component
@SuppressWarnings("UnstableApiUsage")
public class World {
    private static final double MAX_GROUND_CROSSING_DISTANCE_KM = 100;

    private final Map<Code, Integer> codeMapper = new HashMap<>();
    private final Map<Integer, Spot> spots = new HashMap<>();
    private final ValueGraph<Integer, Move> moves;

    public World(AirportDataset airportDataset, RouteDataset routeDataset) {
        MutableValueGraph<Integer, Move> moves = ValueGraphBuilder
                .directed()
                .allowsSelfLoops(false)
                .build();

        AtomicInteger nextId = new AtomicInteger(0);
        airportDataset
                .read()
                .map(airport -> airport.spot(nextId.incrementAndGet()))
                .filter(spot -> !spot.codes.isEmpty())
                .forEach(spot -> {
                    spots.put(spot.id, spot);
                    moves.addNode(spot.id);
                    spot.codes.forEach(code -> codeMapper.put(code, spot.id));
                });

        routeDataset
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
                    Move move = Move.byAir(src, dst, kmDistance(src, dst));
                    moves.putEdgeValue(move.src.id, move.dst.id, move);
                });

        new SegmentationBasedChangeDetector()
                .detect(spots.values(), MAX_GROUND_CROSSING_DISTANCE_KM)
                .forEach(change -> {
                    Move move = Move.byGround(change);
                    moves.putEdgeValue(move.src.id, move.dst.id, move);
                });

        this.moves = moves;
    }

    public Spot spot(Code code) {
        Integer id = codeMapper.get(code);
        if (id == null) throw new SpotNotFoundException(code);
        return spots.get(id);
    }

    public Collection<Move> outgoingMoves(int spotId) {
        Collection<Move> outgoingMoves = new ArrayList<>();
        for (EndpointPair<Integer> edge : moves.incidentEdges(spotId)) {
            if (edge.source() == spotId) {
                moves.edgeValue(edge).ifPresent(outgoingMoves::add);
            }
        }
        return outgoingMoves;
    }
}