package antivoland.transporeon.domain;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Code {
    public enum Type {IATA, ICAO}

    public final Type type;
    public final String value;

    private Code(Type type, String value) {
        this.type = type;
        this.value = value.toUpperCase();
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