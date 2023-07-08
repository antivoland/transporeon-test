package antivoland.transporeon.dataset;

import antivoland.transporeon.model.Code;
import antivoland.transporeon.model.Spot;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public interface Dataset<E> {
    Stream<E> read();

    @Builder
    @EqualsAndHashCode
    class Airport {
        public final Code iataCode;
        public final Code icaoCode;
        public final double lat;
        public final double lon;

        public Spot spot() {
            return new Spot(codes(), lat, lon);
        }

        private List<Code> codes() {
            List<Code> codes = new ArrayList<>();
            if (iataCode != null) codes.add(iataCode);
            if (icaoCode != null) codes.add(icaoCode);
            return codes;
        }
    }

    @Builder
    @EqualsAndHashCode
    class Route {
        public final Code srcAirportCode;
        public final Code dstAirportCode;
        public final boolean direct;
    }
}