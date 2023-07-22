package antivoland.transporeon.api;

import antivoland.transporeon.dataset.AirportDataset;
import antivoland.transporeon.dataset.Dataset;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class DatasetAPI {
    private final AirportDataset airportDataset;

    public DatasetAPI(AirportDataset airportDataset) {
        this.airportDataset = airportDataset;
    }

    @ResponseStatus(OK)
    @GetMapping("datasets/airports")
    Collection<Dataset.Airport> airports() {
        return airportDataset.read().toList();
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    String error(Exception e) {
        return e.getMessage();
    }
}