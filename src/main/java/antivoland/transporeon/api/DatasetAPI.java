package antivoland.transporeon.api;

import antivoland.transporeon.dataset.AirportDataset;
import antivoland.transporeon.dataset.Dataset;
import antivoland.transporeon.dataset.RouteDataset;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class DatasetAPI {
    private final AirportDataset airportDataset;
    private final RouteDataset routeDataset;

    public DatasetAPI(AirportDataset airportDataset, RouteDataset routeDataset) {
        this.airportDataset = airportDataset;
        this.routeDataset = routeDataset;
    }

    @GetMapping("airports")
    Collection<Dataset.Airport> airports() {
        return airportDataset.read().toList();
    }

    @GetMapping("routes")
    Collection<Dataset.Route> routes() {
        return routeDataset.read().toList();
    }
}