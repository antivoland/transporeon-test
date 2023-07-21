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
    Route route(@RequestParam("srcCode") String srcCode, @RequestParam("dstCode") String dstCode) {
        return router.findShortestRoute(Code.code(srcCode), Code.code(dstCode));
    }

    // todo: exception handlers

    public static void main(String... args) {
        SpringApplication.run(FlightConnections.class, args);
    }
}