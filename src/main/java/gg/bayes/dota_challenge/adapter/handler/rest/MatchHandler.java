package gg.bayes.dota_challenge.adapter.handler.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/match")
public class MatchHandler {

    @PostMapping(value = "", consumes = MediaType.TEXT_PLAIN_VALUE)
    public String createMatch(@RequestBody String combatLog) {
        System.out.println(combatLog);
        return "OK";
    }
}
