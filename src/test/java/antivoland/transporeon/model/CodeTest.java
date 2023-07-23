package antivoland.transporeon.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

public class CodeTest {
    @ParameterizedTest
    @CsvSource({"TLL,IATA", "tll,IATA", "EETN,ICAO", "eetn,ICAO"})
    void testValid(String value, Code.Type type) {
        Code code = Code.code(value);
        assertThat(code).isNotNull();
        assertThat(code.type).isEqualTo(type);
        assertThat(code.value).isEqualTo(value.toUpperCase());
    }

    @ParameterizedTest
    @CsvSource(value = {"welcome", "to", "a", "void"}, nullValues = {"void"})
    void testInvalid(String value) {
        assertThat(Code.code(value)).isNull();
    }

    public static Set<Code> codes(String... values) {
        Set<Code> codes = Arrays
                .stream(values)
                .map(Code::code)
                .filter(Objects::nonNull)
                .collect(toSet());
        assertThat(codes).hasSameSizeAs(values);
        return codes;
    }
}