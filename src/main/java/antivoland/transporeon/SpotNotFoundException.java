package antivoland.transporeon;

import antivoland.transporeon.model.Code;

import static java.lang.String.format;

public class SpotNotFoundException extends RuntimeException {
    public SpotNotFoundException(Code code) {
        super(format("Spot with code %s not found", code));
    }
}