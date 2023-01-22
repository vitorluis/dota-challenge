package gg.bayes.dota_challenge.application;

import gg.bayes.dota_challenge.adapter.dto.HeroDamage;
import gg.bayes.dota_challenge.adapter.dto.HeroItem;
import gg.bayes.dota_challenge.adapter.dto.HeroKills;
import gg.bayes.dota_challenge.adapter.dto.HeroSpells;
import gg.bayes.dota_challenge.adapter.repository.CombatLogEntryRepository;
import gg.bayes.dota_challenge.adapter.repository.MatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueryMatchServiceTest {
    @Mock
    private MatchRepository matchRepository;

    @Mock
    private CombatLogEntryRepository combatLogEntryRepository;

    private QueryMatchService queryMatchService;

    @BeforeEach
    void setUp() {
        queryMatchService = new QueryMatchService(matchRepository, combatLogEntryRepository);
    }

    @Test
    void getHeroKillsByMatchId() {
        when(matchRepository.existsById(1L)).thenReturn(true);
        when(combatLogEntryRepository.findAllKillsByMatchId(1L)).thenReturn(List.of(new HeroKills("hero", 1)));

        var result = queryMatchService.getHeroKillsByMatchId(1L);

        assertThat(result).isNotNull().isNotEmpty();
        assertThat(result.get(0).hero()).isEqualTo("hero");
        assertThat(result.get(0).kills()).isEqualTo(1);
    }

    @Test
    void getHeroKillsByMatchId_matchNotFound() {
        when(matchRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> queryMatchService.getHeroKillsByMatchId(1L))
            .isInstanceOf(MatchNotFoundException.class);
    }

    @Test
    void getHeroItems() {
        when(matchRepository.existsById(1L)).thenReturn(true);
        when(combatLogEntryRepository.findAllHeroItems(1L, "hero"))
            .thenReturn(List.of(new HeroItem("sword", 2)));

        var result = queryMatchService.getHeroItems(1L, "hero");

        assertThat(result).isNotNull().isNotEmpty();
        assertThat(result.get(0).item()).isEqualTo("sword");
        assertThat(result.get(0).timestamp()).isEqualTo(2);
    }

    @Test
    void getHeroSpell() {
        when(matchRepository.existsById(1L)).thenReturn(true);
        when(combatLogEntryRepository.findAllHeroSpells(1L, "hero"))
            .thenReturn(List.of(new HeroSpells("spell", 10)));

        var result = queryMatchService.getHeroSpells(1L, "hero");

        assertThat(result).isNotNull().isNotEmpty();
        assertThat(result.get(0).spell()).isEqualTo("spell");
        assertThat(result.get(0).casts()).isEqualTo(10);
    }

    @Test
    void getHeroDamage() {
        when(matchRepository.existsById(1L)).thenReturn(true);
        when(combatLogEntryRepository.findAllHeroTakenDamage(1L, "hero"))
            .thenReturn(List.of(new HeroDamage("enemy", 10, 1000)));

        var result = queryMatchService.getHeroDamage(1L, "hero");

        assertThat(result).isNotNull().isNotEmpty();
        assertThat(result.get(0).target()).isEqualTo("enemy");
        assertThat(result.get(0).damageInstances()).isEqualTo(10);
        assertThat(result.get(0).totalDamage()).isEqualTo(1000);
    }
}