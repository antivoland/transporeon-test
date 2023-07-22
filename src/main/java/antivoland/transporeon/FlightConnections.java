package antivoland.transporeon;

import antivoland.transporeon.model.Code;
import antivoland.transporeon.model.route.Route;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
class FlightConnections {

    private final Router router;

    public FlightConnections(Router router) {
        this.router = router;
    }

    @GetMapping("route")
    Route route(@RequestParam(value = "srcCode") String srcCode,
                @RequestParam(value = "dstCode") String dstCode,
                @RequestParam(value = "limited", defaultValue = "true") boolean limited) {
        return router.findShortestRoute(Code.code(srcCode), Code.code(dstCode), limited);
    }

    // todo: exception handlers

    public static void main(String... args) {
        SpringApplication.run(FlightConnections.class, args);
    }
}