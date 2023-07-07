package antivoland.transporeon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
class FlightConnections {
    @GetMapping("route")
    Object route(@RequestParam("src") String src, @RequestParam("dst") String dst) {
        return "Not implemented yet";
    }

    public static void main(String... args) {
        SpringApplication.run(FlightConnections.class, args);
    }
}