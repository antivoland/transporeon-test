package antivoland.transporeon.model;

import lombok.EqualsAndHashCode;

import static java.lang.String.format;

@EqualsAndHashCode
public class Code {
    public enum Type {IATA, ICAO}

    public final Type type;
    public final String value;

    private Code(Type type, String value) {
        this.type = type;
        this.value = value.toUpperCase();
    }

    @Override
    public String toString() {
        return format("%s(%s)", type, value);
    }

    public static Code code(String value) {
        if (looksLikeIATACode(value)) return new Code(Type.IATA, value);
        if (looksLikeICAOCode(value)) return new Code(Type.ICAO, value);
        return null;
    }

    private static boolean looksLikeIATACode(String value) {
        return value != null && value.length() == 3;
    }

    private static boolean looksLikeICAOCode(String value) {
        return value != null && value.length() == 4;
    }
}