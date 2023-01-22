package gg.bayes.dota_challenge.application;

import gg.bayes.dota_challenge.adapter.dto.HeroItem;
import gg.bayes.dota_challenge.adapter.dto.HeroKills;
import gg.bayes.dota_challenge.adapter.dto.HeroSpells;
import gg.bayes.dota_challenge.adapter.repository.CombatLogEntryRepository;
import gg.bayes.dota_challenge.adapter.repository.MatchRepository;
import gg.bayes.dota_challenge.domain.Match;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueryMatchService {
    private final MatchRepository matchRepository;
    private final CombatLogEntryRepository combatLogEntryRepository;

    public List<HeroKills> getHeroKillsByMatchId(long matchId) {
        // To do some proper error handling, let's double-check if the match really exists
        return combatLogEntryRepository.findAllKillsByMatchId(getMatchById(matchId).getId());
    }

    public List<HeroItem> getHeroItems(long matchId, String hero) {
        return combatLogEntryRepository.findAllHeroItems(getMatchById(matchId).getId(), hero);
    }

    public List<HeroSpells> getHeroSpell(long matchId, String hero) {
        return combatLogEntryRepository.findAllHeroSpells(getMatchById(matchId).getId(), hero);
    }

    private Match getMatchById(long matchId) {
        return matchRepository.findById(matchId).orElseThrow(() -> {
            throw new MatchNotFoundException("Match with ID " + matchId + " not found.");
        });
    }
}
