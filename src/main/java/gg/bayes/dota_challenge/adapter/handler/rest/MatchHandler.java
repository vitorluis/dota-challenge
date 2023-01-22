package gg.bayes.dota_challenge.adapter.handler.rest;

import gg.bayes.dota_challenge.adapter.dto.HeroDamage;
import gg.bayes.dota_challenge.adapter.dto.HeroItem;
import gg.bayes.dota_challenge.adapter.dto.HeroKills;
import gg.bayes.dota_challenge.adapter.dto.HeroSpells;
import gg.bayes.dota_challenge.adapter.dto.MatchResponse;
import gg.bayes.dota_challenge.application.CreateMatchService;
import gg.bayes.dota_challenge.application.QueryMatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/match")
public class MatchHandler {
    private final CreateMatchService createMatchService;
    private final QueryMatchService queryMatchService;

    @PostMapping(value = "", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public MatchResponse createMatch(@RequestBody String combatLog) {
        var entries = Arrays.asList(combatLog.split("\n"));
        var match = createMatchService.createMatch(entries);

        return new MatchResponse(match.getId());
    }

    @GetMapping(value = "/{matchId}")
    @ResponseBody
    public List<HeroKills> getHeroKills(@PathVariable("matchId") Long id) {
        return queryMatchService.getHeroKillsByMatchId(id);
    }

    @GetMapping(value = "/{matchId}/{heroName}/items")
    @ResponseBody
    public List<HeroItem> getHeroItems(@PathVariable("matchId") Long id, @PathVariable("heroName") String heroName) {
        return queryMatchService.getHeroItems(id, heroName);
    }

    @GetMapping(value = "/{matchId}/{heroName}/spells")
    @ResponseBody
    public List<HeroSpells> getHeroSpells(@PathVariable("matchId") Long id, @PathVariable("heroName") String heroName) {
        return queryMatchService.getHeroSpells(id, heroName);
    }

    @GetMapping(value = "/{matchId}/{heroName}/damage")
    @ResponseBody
    public List<HeroDamage> getHeroDamage(@PathVariable("matchId") Long id, @PathVariable("heroName") String heroName) {
        return queryMatchService.getHeroDamage(id, heroName);
    }
}
