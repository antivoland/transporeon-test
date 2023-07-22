package antivoland.transporeon.exception;

import antivoland.transporeon.model.Code;

import static java.lang.String.format;

public class RouteNotFoundException extends RuntimeException {
    public RouteNotFoundException(Code srcAirportCode, Code dstAirportCode) {
        super(format("Route from %s to %s not found", srcAirportCode, dstAirportCode));
    }
}