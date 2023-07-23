package antivoland.transporeon.model;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

// todo: implement
public class CodeTest {
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