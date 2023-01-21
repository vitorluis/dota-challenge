package gg.bayes.dota_challenge.adapter.handler.rest;

import gg.bayes.dota_challenge.application.CreateMatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequiredArgsConstructor
@RequestMapping("/match")
public class MatchHandler {
    private final CreateMatchService createMatchService;

    @PostMapping(value = "", consumes = MediaType.TEXT_PLAIN_VALUE)
    public String createMatch(@RequestBody String combatLog) {
        var entries = Arrays.asList(combatLog.split("\n"));
        createMatchService.createMatch(entries);
        return "OK";
    }
}
