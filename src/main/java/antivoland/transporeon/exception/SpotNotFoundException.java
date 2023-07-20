package antivoland.transporeon.exception;

import antivoland.transporeon.model.Code;

import static java.lang.String.format;

public class SpotNotFoundException extends RuntimeException {
    public SpotNotFoundException(Code code) {
        super(format("Spot '%s' not found", code));
    }
}