package antivoland.transporeon.dataset;

import java.util.stream.Stream;

public interface Dataset<E> {
    Stream<E> read();
}