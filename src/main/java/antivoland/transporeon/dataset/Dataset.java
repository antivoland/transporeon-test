package antivoland.transporeon.dataset;

import antivoland.transporeon.model.Code;
import antivoland.transporeon.model.Spot;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public interface Dataset<E> {
    Stream<E> read();

    @Builder
    @ToString
    @EqualsAndHashCode
    class Airport {
        public final Code iataCode;
        public final Code icaoCode;
        public final double lat;
        public final double lon;

        public Set<Code> codes() {
            Set<Code> codes = new HashSet<>();
            if (iataCode != null) codes.add(iataCode);
            if (icaoCode != null) codes.add(icaoCode);
            return codes;
        }

        public Spot spot(int id) {
            return new Spot(id, lat, lon, codes());
        }
    }

    @Builder
    @ToString
    @EqualsAndHashCode
    class Route {
        public final Code srcAirportCode;
        public final Code dstAirportCode;
        public final boolean direct;
    }
}