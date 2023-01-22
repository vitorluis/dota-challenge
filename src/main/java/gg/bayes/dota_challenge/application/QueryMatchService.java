package gg.bayes.dota_challenge.application;

import gg.bayes.dota_challenge.adapter.dto.HeroDamage;
import gg.bayes.dota_challenge.adapter.dto.HeroItem;
import gg.bayes.dota_challenge.adapter.dto.HeroKills;
import gg.bayes.dota_challenge.adapter.dto.HeroSpells;
import gg.bayes.dota_challenge.adapter.repository.CombatLogEntryRepository;
import gg.bayes.dota_challenge.adapter.repository.MatchRepository;
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
        checkIfMatchExists(matchId);
        return combatLogEntryRepository.findAllKillsByMatchId(matchId);
    }

    public List<HeroItem> getHeroItems(long matchId, String hero) {
        // Let's check also if the hero exists. IF the list is empty, let's throw an exception to client
        checkIfMatchExists(matchId);
        return combatLogEntryRepository.findAllHeroItems(matchId, hero);
    }

    public List<HeroSpells> getHeroSpells(long matchId, String hero) {
        // Let's check also if the hero exists. IF the list is empty, let's throw an exception to client
        checkIfMatchExists(matchId);
        return combatLogEntryRepository.findAllHeroSpells(matchId, hero);
    }

    public List<HeroDamage> getHeroDamage(long matchId, String hero) {
        // Let's check also if the hero exists. IF the list is empty, let's throw an exception to client
        checkIfMatchExists(matchId);
        return combatLogEntryRepository.findAllHeroTakenDamage(matchId, hero);
    }

    private void checkIfMatchExists(long matchId) {
        if (!matchRepository.existsById(matchId)) {
            throw new MatchNotFoundException("Match with ID " + matchId + " not found.");
        }
    }
}
