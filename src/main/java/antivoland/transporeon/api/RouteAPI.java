package antivoland.transporeon.api;

import antivoland.transporeon.exception.RouteNotFoundException;
import antivoland.transporeon.exception.SpotNotFoundException;
import antivoland.transporeon.model.Code;
import antivoland.transporeon.model.Router;
import antivoland.transporeon.model.route.Route;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
public class RouteAPI {
    private final Router router;

    public RouteAPI(Router router) {
        this.router = router;
    }

    @ResponseStatus(OK)
    @GetMapping("routes/shortest")
    Route route(@RequestParam(value = "srcCode") String srcCode,
                @RequestParam(value = "dstCode") String dstCode,
                @RequestParam(value = "limited", defaultValue = "true") boolean limited) {
        return router.findShortestRoute(srcCode, dstCode, limited);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(SpotNotFoundException.class)
    String spotNotFound(SpotNotFoundException e) {
        return e.getMessage();
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(RouteNotFoundException.class)
    String spotNotFound(RouteNotFoundException e) {
        return e.getMessage();
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    String error(Exception e) {
        return e.getMessage();
    }
}