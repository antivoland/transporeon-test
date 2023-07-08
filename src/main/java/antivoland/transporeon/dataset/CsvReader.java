package antivoland.transporeon.dataset;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.SneakyThrows;

import java.nio.file.Path;
import java.util.stream.Stream;

import static com.fasterxml.jackson.dataformat.csv.CsvParser.Feature.WRAP_AS_ARRAY;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.StreamSupport.stream;

class CsvReader {
    private static final CsvMapper MAPPER = new CsvMapper().enable(WRAP_AS_ARRAY);

    @SneakyThrows
    static Stream<String[]> read(Path file) {
        CsvSchema schema = CsvSchema.emptySchema().withoutHeader();
        MappingIterator<String[]> iterator = MAPPER.readerFor(String[].class)
                .with(schema)
                .readValues(file.toFile());
        return stream(spliteratorUnknownSize(iterator, ORDERED), false);
    }
}