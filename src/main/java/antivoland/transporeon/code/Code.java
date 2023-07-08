package antivoland.transporeon.code;

public class Code {
    public final CodeType type;
    public final String value;

    Code(CodeType type, String value) {
        this.type = type;
        this.value = value.toUpperCase();
    }

    public static Code code(String value) {
        if (looksLikeIATACode(value)) return new IATACode(value);
        if (looksLikeICAOCode(value)) return new ICAOCode(value);
        return null;
    }

    static boolean looksLikeIATACode(String value) {
        return value != null && value.length() == 3;
    }

    static boolean looksLikeICAOCode(String value) {
        return value != null && value.length() == 4;
    }
}