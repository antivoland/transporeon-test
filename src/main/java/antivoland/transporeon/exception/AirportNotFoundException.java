package antivoland.transporeon.exception;

import antivoland.transporeon.model.Code;

import static java.lang.String.format;

public class AirportNotFoundException extends RuntimeException {
    public AirportNotFoundException(Code airportCode) {
        super(format("Airport '%s' not found", airportCode));
    }
}