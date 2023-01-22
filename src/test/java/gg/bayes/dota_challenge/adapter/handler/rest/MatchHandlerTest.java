package gg.bayes.dota_challenge.adapter.handler.rest;

import gg.bayes.dota_challenge.adapter.dto.HeroDamage;
import gg.bayes.dota_challenge.adapter.dto.HeroItem;
import gg.bayes.dota_challenge.adapter.dto.HeroKills;
import gg.bayes.dota_challenge.adapter.dto.HeroSpells;
import gg.bayes.dota_challenge.application.CreateMatchService;
import gg.bayes.dota_challenge.application.MatchNotFoundException;
import gg.bayes.dota_challenge.application.QueryMatchService;
import gg.bayes.dota_challenge.domain.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(MatchHandler.class)
class MatchHandlerTest {
    private static final String CREATE_MATCH_ENDPOINT = "/match";
    private static final String GET_HERO_KILLS = "/match/1";
    private static final String GET_HERO_ITEMS = "/match/1/hero/items";
    private static final String GET_HERO_SPELLS = "/match/1/hero/spells";
    private static final String GET_HERO_DAMAGE = "/match/1/hero/damage";

    @MockBean
    private CreateMatchService createMatchService;

    @MockBean
    private QueryMatchService queryMatchService;

    private MockMvc mockMvc;

    @InjectMocks
    private ControllerAdvicer controllerAdvice;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(new MatchHandler(createMatchService, queryMatchService))
            .setControllerAdvice(controllerAdvice)
            .build();
    }

    @Test
    void createMatch() throws Exception {
        var match = new Match(1L, Set.of());
        when(createMatchService.createMatch(List.of("events"))).thenReturn(match);

        // As this is a unit test, the ID of the match is not available
        mockMvc.perform(post(CREATE_MATCH_ENDPOINT).contentType(MediaType.TEXT_PLAIN_VALUE).content("events"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void getHeroKills() throws Exception {
        when(queryMatchService.getHeroKillsByMatchId(1)).thenReturn(List.of(new HeroKills("hero", 1)));

        // As this is a unit test, the ID of the match is not available
        mockMvc.perform(get(GET_HERO_KILLS).contentType(MediaType.APPLICATION_JSON_VALUE).content("events"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$[0].hero", is("hero")))
            .andExpect(jsonPath("$[0].kills", is(1)));
    }

    @Test
    void getHeroKills_matchNotFound() throws Exception {
        when(queryMatchService.getHeroKillsByMatchId(1)).thenThrow(new MatchNotFoundException("Match not Found"));

        // As this is a unit test, the ID of the match is not available
        mockMvc.perform(get(GET_HERO_KILLS).contentType(MediaType.APPLICATION_JSON_VALUE).content("events"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Match not Found")));
    }

    @Test
    void getHeroItems() throws Exception {
        when(queryMatchService.getHeroItems(1, "hero"))
            .thenReturn(List.of(new HeroItem("sword", 1)));

        // As this is a unit test, the ID of the match is not available
        mockMvc.perform(get(GET_HERO_ITEMS).contentType(MediaType.APPLICATION_JSON_VALUE).content("events"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$[0].item", is("sword")))
            .andExpect(jsonPath("$[0].timestamp", is(1)));
    }

    @Test
    void getHeroSpells() throws Exception {
        when(queryMatchService.getHeroSpells(1, "hero"))
            .thenReturn(List.of(new HeroSpells("spell", 1)));

        // As this is a unit test, the ID of the match is not available
        mockMvc.perform(get(GET_HERO_SPELLS).contentType(MediaType.APPLICATION_JSON_VALUE).content("events"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$[0].spell", is("spell")))
            .andExpect(jsonPath("$[0].casts", is(1)));
    }

    @Test
    void getHeroDamage() throws Exception {
        when(queryMatchService.getHeroDamage(1, "hero"))
            .thenReturn(List.of(new HeroDamage("enemy", 10, 1000)));

        // As this is a unit test, the ID of the match is not available
        mockMvc.perform(get(GET_HERO_DAMAGE).contentType(MediaType.APPLICATION_JSON_VALUE).content("events"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$[0].target", is("enemy")))
            .andExpect(jsonPath("$[0].damage_instances", is(10)))
            .andExpect(jsonPath("$[0].total_damage", is(1000)));
    }
}