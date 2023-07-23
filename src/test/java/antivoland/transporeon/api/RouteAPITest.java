package antivoland.transporeon.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RouteAPITest {
    @Autowired
    MockMvc api;

    @Test
    void test() throws Exception {
        api.perform(get("/routes/shortest?srcCode=TLL&dstCode=BUZ"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.moves", hasSize(6)))
                .andExpect(jsonPath("$.moves[0].type", is("BY_AIR")))
                .andExpect(jsonPath("$.moves[0].src.codes[*].value", containsInAnyOrder("TLL", "EETN")))
                .andExpect(jsonPath("$.moves[0].dst.codes[*].value", containsInAnyOrder("SVO", "UUEE")))
                .andExpect(jsonPath("$.moves[1].type", is("BY_GROUND")))
                .andExpect(jsonPath("$.moves[1].src.codes[*].value", containsInAnyOrder("SVO", "UUEE")))
                .andExpect(jsonPath("$.moves[1].dst.codes[*].value", containsInAnyOrder("DME", "UUDD")))
                .andExpect(jsonPath("$.moves[2].type", is("BY_AIR")))
                .andExpect(jsonPath("$.moves[2].src.codes[*].value", containsInAnyOrder("DME", "UUDD")))
                .andExpect(jsonPath("$.moves[2].dst.codes[*].value", containsInAnyOrder("LLK", "UBBL")))
                .andExpect(jsonPath("$.moves[3].type", is("BY_GROUND")))
                .andExpect(jsonPath("$.moves[3].src.codes[*].value", containsInAnyOrder("LLK", "UBBL")))
                .andExpect(jsonPath("$.moves[3].dst.codes[*].value", containsInAnyOrder("ADU", "OITL")))
                .andExpect(jsonPath("$.moves[4].type", is("BY_AIR")))
                .andExpect(jsonPath("$.moves[4].src.codes[*].value", containsInAnyOrder("ADU", "OITL")))
                .andExpect(jsonPath("$.moves[4].dst.codes[*].value", containsInAnyOrder("THR", "OIII")))
                .andExpect(jsonPath("$.moves[5].type", is("BY_AIR")))
                .andExpect(jsonPath("$.moves[5].src.codes[*].value", containsInAnyOrder("THR", "OIII")))
                .andExpect(jsonPath("$.moves[5].dst.codes[*].value", containsInAnyOrder("BUZ", "OIBB")))
                .andExpect(jsonPath("$.kmDistance", closeTo(4131.04, 1e-2)));
    }

    @Test
    void testNotFound() throws Exception {
        api.perform(get("/routes/shortest?srcCode=TLL&dstCode=AGS"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Route from IATA(TLL) to IATA(AGS) not found"));
    }
}