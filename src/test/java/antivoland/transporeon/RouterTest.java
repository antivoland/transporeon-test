package antivoland.transporeon;

import antivoland.transporeon.model.Code;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
@SpringBootTest
class RouterTest {
    @Autowired
    Router router;

    @Test
    void test() {
        Route route = router.findShortestRoute(Code.code("TLL"), Code.code("LYR"));
        System.out.println(route);
    }
}