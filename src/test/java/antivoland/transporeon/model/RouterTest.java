package antivoland.transporeon.model;

import antivoland.transporeon.exception.RouteNotFoundException;
import antivoland.transporeon.model.route.MoveType;
import antivoland.transporeon.model.route.Route;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static antivoland.transporeon.model.CodeTest.codes;
import static antivoland.transporeon.model.DistanceCalculator.kmDistance;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class RouterTest {
    static final double EPSILON = 10e-6;

    @Autowired
    Router router;

    /*
     Svalbard airport is the northernmost airport in the world. It is possible
     to get there by air either from Oslo or from Tromsø:

     grep -E '^.*,[A-Za-z0-9]{3,4},.*,(LYR|ENSB),.*$' routes.dat
     DY,3737,OSL,644,LYR,658,,0,73H
     SK,4319,OSL,644,LYR,658,,0,738 73W
     SK,4319,TOS,663,LYR,658,,0,73H 738 73W

     And I want to travel there from Tallinn, so the route will include either
     Oslo or Tromsø:

     grep -E '^.*,(TLL|EETN),.*,(OSL|ENGM|TOS|ENTC),.*$' routes.dat
     DY,3737,TLL,415,OSL,644,,0,733 73H
     OV,2218,TLL,415,OSL,644,,0,E70
     SK,4319,TLL,415,OSL,644,Y,0,E70

     There are direct flights from Tallinn to Oslo. Tromsø is located north of
     Oslo and much closer to the end point of the route. And if we repeat the
     flights exploration then we'll find the better route including a stop in
     Stockholm Arlanda Airport: (TLL|EETN)->(ARN|ESSA)->(TOS|ENTC)->(LYR|ENSB)
     */
    @ParameterizedTest
    @CsvSource({"TLL,LYR", "TLL,ENSB", "EETN,LYR", "EETN,ENSB"})
    void testTravelToSvalbard(String srcCode, String dstCode) {
        Route limited = router.findShortestRoute(srcCode, dstCode);
        assertThat(limited).isNotNull();
        assertThat(limited.moves).isNotNull().hasSize(3);

        assertThat(limited.moves[0].type).isEqualTo(MoveType.BY_AIR);
        assertThat(limited.moves[0].src.codes).isEqualTo(codes("TLL", "EETN"));
        assertThat(limited.moves[0].dst.codes).isEqualTo(codes("ARN", "ESSA"));

        assertThat(limited.moves[1].type).isEqualTo(MoveType.BY_AIR);
        assertThat(limited.moves[1].src.codes).isEqualTo(codes("ARN", "ESSA"));
        assertThat(limited.moves[1].dst.codes).isEqualTo(codes("TOS", "ENTC"));

        assertThat(limited.moves[2].type).isEqualTo(MoveType.BY_AIR);
        assertThat(limited.moves[2].src.codes).isEqualTo(codes("TOS", "ENTC"));
        assertThat(limited.moves[2].dst.codes).isEqualTo(codes("LYR", "ENSB"));

        Route unlimited = router.findShortestRoute(srcCode, dstCode, false);
        assertThat(unlimited.kmDistance).isCloseTo(limited.kmDistance, offset(EPSILON));
    }

    /*
     Testing shortest routes is an uneasy activity. At the moment I have no
     better idea than finding some interesting routes via the project's UI and
     testing some particular stuff. At the moment I have no better idea than to
     find some interesting routes using the project's UI and test some
     particular things.

     For instance, there is a route from Tallinn to Andrau Airpark, which is
     not much longer than just the distance between these locations.
     */
    @ParameterizedTest
    @CsvSource({"TLL,AAP", "TLL,KAAP", "EETN,AAP", "EETN,KAAP"})
    void testTravelToAndrauAirpark(String srcCode, String dstCode) {
        Route limited = router.findShortestRoute(srcCode, dstCode);
        assertThat(limited).isNotNull();
        assertThat(limited.moves).isNotNull().hasSize(5);

        assertThat(limited.moves[0].type).isEqualTo(MoveType.BY_AIR);
        assertThat(limited.moves[0].src.codes).isEqualTo(codes("TLL", "EETN"));
        assertThat(limited.moves[0].dst.codes).isEqualTo(codes("TRD", "ENVA"));

        assertThat(limited.moves[1].type).isEqualTo(MoveType.BY_AIR);
        assertThat(limited.moves[1].src.codes).isEqualTo(codes("TRD", "ENVA"));
        assertThat(limited.moves[1].dst.codes).isEqualTo(codes("KEF", "BIKF"));

        assertThat(limited.moves[2].type).isEqualTo(MoveType.BY_AIR);
        assertThat(limited.moves[2].src.codes).isEqualTo(codes("KEF", "BIKF"));
        assertThat(limited.moves[2].dst.codes).isEqualTo(codes("YYZ", "CYYZ"));

        assertThat(limited.moves[3].type).isEqualTo(MoveType.BY_AIR);
        assertThat(limited.moves[3].src.codes).isEqualTo(codes("YYZ", "CYYZ"));
        assertThat(limited.moves[3].dst.codes).isEqualTo(codes("IAH", "KIAH"));

        assertThat(limited.moves[4].type).isEqualTo(MoveType.BY_GROUND);
        assertThat(limited.moves[4].src.codes).isEqualTo(codes("IAH", "KIAH"));
        assertThat(limited.moves[4].dst.codes).isEqualTo(codes("AAP", "KAAP"));

        double kmDistance = kmDistance(limited.moves[0].src, limited.moves[4].dst);
        assertThat(limited.kmDistance).isCloseTo(kmDistance, offset(60.0));

        Route unlimited = router.findShortestRoute(srcCode, dstCode, false);
        assertThat(unlimited.kmDistance).isCloseTo(limited.kmDistance, offset(EPSILON));
    }

    /*
     Another even better example is an unlimited route from Tallinn to Augusta
     Regional Airport, which is only 10 km longer than just the distance
     between locations. And the limited route doesn't exist in this case.
     */
    @ParameterizedTest
    @CsvSource({"TLL,AGS", "TLL,KAGS", "EETN,AGS", "EETN,KAGS"})
    void testTravelToAugustaRegionalAirport(String srcCode, String dstCode) {
        Route unlimited = router.findShortestRoute(srcCode, dstCode, false);
        assertThat(unlimited).isNotNull();
        assertThat(unlimited.moves).isNotNull().hasSize(5);

        assertThat(unlimited.moves[0].type).isEqualTo(MoveType.BY_AIR);
        assertThat(unlimited.moves[0].src.codes).isEqualTo(codes("TLL", "EETN"));
        assertThat(unlimited.moves[0].dst.codes).isEqualTo(codes("TRD", "ENVA"));

        assertThat(unlimited.moves[1].type).isEqualTo(MoveType.BY_AIR);
        assertThat(unlimited.moves[1].src.codes).isEqualTo(codes("TRD", "ENVA"));
        assertThat(unlimited.moves[1].dst.codes).isEqualTo(codes("KEF", "BIKF"));

        assertThat(unlimited.moves[2].type).isEqualTo(MoveType.BY_AIR);
        assertThat(unlimited.moves[2].src.codes).isEqualTo(codes("KEF", "BIKF"));
        assertThat(unlimited.moves[2].dst.codes).isEqualTo(codes("IAD", "KIAD"));

        assertThat(unlimited.moves[3].type).isEqualTo(MoveType.BY_AIR);
        assertThat(unlimited.moves[3].src.codes).isEqualTo(codes("IAD", "KIAD"));
        assertThat(unlimited.moves[3].dst.codes).isEqualTo(codes("CLT", "KCLT"));

        assertThat(unlimited.moves[4].type).isEqualTo(MoveType.BY_AIR);
        assertThat(unlimited.moves[4].src.codes).isEqualTo(codes("CLT", "KCLT"));
        assertThat(unlimited.moves[4].dst.codes).isEqualTo(codes("AGS", "KAGS"));

        double kmDistance = kmDistance(unlimited.moves[0].src, unlimited.moves[4].dst);
        assertThat(unlimited.kmDistance).isCloseTo(kmDistance, offset(10.0));

        assertThatThrownBy(() -> router.findShortestRoute(srcCode, dstCode))
                .isInstanceOf(RouteNotFoundException.class);
    }

    /*
     Now let's use Antsirabato Airport as an example to demonstrate that the
     limited route there from Tallinn contains fewer stops, but the route
     distance is longer compared to the unlimited version.
     */
    @ParameterizedTest
    @CsvSource({"TLL,ANM", "TLL,FMNH", "EETN,ANM", "EETN,FMNH"})
    void testTravelToAntsirabatoAirport(String srcCode, String dstCode) {
        Route unlimited = router.findShortestRoute(srcCode, dstCode, false);
        assertThat(unlimited).isNotNull();
        assertThat(unlimited.moves).isNotNull().hasSize(9);

        assertThat(unlimited.moves[0].type).isEqualTo(MoveType.BY_AIR);
        assertThat(unlimited.moves[0].src.codes).isEqualTo(codes("TLL", "EETN"));
        assertThat(unlimited.moves[0].dst.codes).isEqualTo(codes("KBP", "UKBB"));

        assertThat(unlimited.moves[1].type).isEqualTo(MoveType.BY_AIR);
        assertThat(unlimited.moves[1].src.codes).isEqualTo(codes("KBP", "UKBB"));
        assertThat(unlimited.moves[1].dst.codes).isEqualTo(codes("TLV", "LLBG"));

        assertThat(unlimited.moves[2].type).isEqualTo(MoveType.BY_AIR);
        assertThat(unlimited.moves[2].src.codes).isEqualTo(codes("TLV", "LLBG"));
        assertThat(unlimited.moves[2].dst.codes).isEqualTo(codes("ADD", "HAAB"));

        assertThat(unlimited.moves[3].type).isEqualTo(MoveType.BY_AIR);
        assertThat(unlimited.moves[3].src.codes).isEqualTo(codes("ADD", "HAAB"));
        assertThat(unlimited.moves[3].dst.codes).isEqualTo(codes("MBA", "HKMO"));

        assertThat(unlimited.moves[4].type).isEqualTo(MoveType.BY_AIR);
        assertThat(unlimited.moves[4].src.codes).isEqualTo(codes("MBA", "HKMO"));
        assertThat(unlimited.moves[4].dst.codes).isEqualTo(codes("HAH", "FMCH"));

        assertThat(unlimited.moves[5].type).isEqualTo(MoveType.BY_AIR);
        assertThat(unlimited.moves[5].src.codes).isEqualTo(codes("HAH", "FMCH"));
        assertThat(unlimited.moves[5].dst.codes).isEqualTo(codes("DZA", "FMCZ"));

        assertThat(unlimited.moves[6].type).isEqualTo(MoveType.BY_AIR);
        assertThat(unlimited.moves[6].src.codes).isEqualTo(codes("DZA", "FMCZ"));
        assertThat(unlimited.moves[6].dst.codes).isEqualTo(codes("DIE", "FMNA"));

        assertThat(unlimited.moves[7].type).isEqualTo(MoveType.BY_AIR);
        assertThat(unlimited.moves[7].src.codes).isEqualTo(codes("DIE", "FMNA"));
        assertThat(unlimited.moves[7].dst.codes).isEqualTo(codes("SVB", "FMNS"));

        assertThat(unlimited.moves[8].type).isEqualTo(MoveType.BY_GROUND);
        assertThat(unlimited.moves[8].src.codes).isEqualTo(codes("SVB", "FMNS"));
        assertThat(unlimited.moves[8].dst.codes).isEqualTo(codes("ANM", "FMNH"));

        Route limited = router.findShortestRoute(srcCode, dstCode);
        assertThat(limited).isNotNull();
        assertThat(limited.moves).isNotNull().hasSize(4);

        assertThat(limited.moves[0].type).isEqualTo(MoveType.BY_AIR);
        assertThat(limited.moves[0].src.codes).isEqualTo(codes("TLL", "EETN"));
        assertThat(limited.moves[0].dst.codes).isEqualTo(codes("IST", "LTFM"));

        assertThat(limited.moves[1].type).isEqualTo(MoveType.BY_AIR);
        assertThat(limited.moves[1].src.codes).isEqualTo(codes("IST", "LTFM"));
        assertThat(limited.moves[1].dst.codes).isEqualTo(codes("NBO", "HKJK"));

        assertThat(limited.moves[2].type).isEqualTo(MoveType.BY_AIR);
        assertThat(limited.moves[2].src.codes).isEqualTo(codes("NBO", "HKJK"));
        assertThat(limited.moves[2].dst.codes).isEqualTo(codes("TNR", "FMMI"));

        assertThat(limited.moves[3].type).isEqualTo(MoveType.BY_AIR);
        assertThat(limited.moves[3].src.codes).isEqualTo(codes("TNR", "FMMI"));
        assertThat(limited.moves[3].dst.codes).isEqualTo(codes("ANM", "FMNH"));

        assertThat(limited.kmDistance - unlimited.kmDistance)
                .isCloseTo(500, offset(15.0));
    }
}