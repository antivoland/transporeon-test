package antivoland.transporeon.model.route;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;

@ToString
public class Route {
    public final Move[] moves;
    public final double kmDistance;
    @JsonIgnore
    public final Stop lastStop;

    public Route(int srcId) {
        this(new Move[0], Stop.first(srcId));
    }

    private Route(Move[] moves, Stop lastStop) {
        this.moves = moves;
        double kmDistance = 0;
        for (Move move : moves) {
            kmDistance += move.kmDistance;
        }
        this.kmDistance = kmDistance;
        this.lastStop = lastStop;
    }

    public boolean canMove(Move move) {
        return lastStop.type != StopType.ENTERED_BY_GROUND || move.type != MoveType.BY_GROUND;
    }

    public Route move(Move move) {
        Move[] moves = new Move[this.moves.length + 1];
        System.arraycopy(this.moves, 0, moves, 0, this.moves.length);
        moves[this.moves.length] = move;
        return new Route(moves, Stop.enteredBy(move));
    }

    public int numberOfFlights() {
        int numberOfFlights = 0;
        for (Move move : moves) {
            if (move.type == MoveType.BY_AIR) ++numberOfFlights;
        }
        return numberOfFlights;
    }
}