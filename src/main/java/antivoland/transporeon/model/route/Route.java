package antivoland.transporeon.model.route;

import lombok.ToString;

@ToString
public class Route {
    public final Stop[] stops;
    public final Move[] moves;
    public final double kmDistance;

    public Route(Stop src) {
        this(new Stop[]{src}, new Move[]{});
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

    public int numberOfMoves(MoveType type) {
        int numberOfMoves = 0;
        for (Move move : moves) {
            if (move.type == type) ++numberOfMoves;
        }
        return numberOfMoves;
    }

    public Stop dst() {
        return stops[stops.length - 1];
    }

    public Route move(Move move, Stop stop) {
        Stop[] newStops = new Stop[stops.length + 1];
        System.arraycopy(stops, 0, newStops, 0, stops.length);
        newStops[stops.length] = stop;

        Move[] newMoves = new Move[moves.length + 1];
        System.arraycopy(moves, 0, newMoves, 0, moves.length);
        newMoves[moves.length + 1] = move;
        return new Route(newStops, newMoves);
    }
}