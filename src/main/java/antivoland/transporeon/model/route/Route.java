package antivoland.transporeon.model.route;

import lombok.ToString;

@ToString
public class Route {
    public final Stop[] stops;
    public final Move[] moves;
    public final double kmDistance;

    public Route(Stop firstStop) {
        this(new Stop[]{firstStop}, new Move[]{});
    }

    public Stop lastStop() {
        return stops[stops.length - 1];
    }

    private Route(Stop[] stops, Move[] moves) {
        this.stops = stops;
        this.moves = moves;
        double kmDistance = 0;
        for (Move move : moves) {
            kmDistance += move.kmDistance;
        }
        this.kmDistance = kmDistance;
    }

    public Route moveTo(Stop stop, Move move) {
        Stop[] newStops = new Stop[stops.length + 1];
        System.arraycopy(stops, 0, newStops, 0, stops.length);
        newStops[stops.length] = stop;

        Move[] newMoves = new Move[moves.length + 1];
        System.arraycopy(moves, 0, newMoves, 0, moves.length);
        newMoves[moves.length + 1] = move;
        return new Route(newStops, newMoves);
    }

    public int numberOfFlights() {
        int numberOfFlights = 0;
        for (Move move : moves) {
            if (move.type == MoveType.BY_AIR) ++numberOfFlights;
        }
        return numberOfFlights;
    }
}