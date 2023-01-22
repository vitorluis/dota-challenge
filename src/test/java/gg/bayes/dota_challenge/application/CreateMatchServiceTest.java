package gg.bayes.dota_challenge.application;

import gg.bayes.dota_challenge.adapter.repository.CombatLogEntryRepository;
import gg.bayes.dota_challenge.adapter.repository.MatchRepository;
import gg.bayes.dota_challenge.domain.CombatLogEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateMatchServiceTest {
    @Mock
    private MatchRepository matchRepository;

    @Mock
    private CombatLogEntryRepository combatLogEntryRepository;

    @Mock
    private PlatformTransactionManager platformTransactionManager;

    private CreateMatchService createMatchService;

    private static Stream<Arguments> testFiles() {
        return Stream.of(
            Arguments.of("classpath:combatlog_1.log.txt", 3784),
            Arguments.of("classpath:combatlog_2.log.txt", 3633)
        );
    }

    @BeforeEach
    void setUp() {
        createMatchService = new CreateMatchService(
            matchRepository,
            combatLogEntryRepository,
            platformTransactionManager
        );
    }

    @ParameterizedTest
    @MethodSource(value = "testFiles")
    void testCreateFullMatch(String testFile, int combatEntries) throws IOException {
        var file = ResourceUtils.getFile(testFile);
        var entries = Files.readAllLines(file.toPath());

        when(matchRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());

        var match = createMatchService.createMatch(entries);

        assertThat(match).isNotNull();
        assertThat(match.getCombatLogEntries()).isNotEmpty().hasSize(combatEntries);

        verify(matchRepository, times(1)).save(any());
        verify(combatLogEntryRepository, times(combatEntries)).save(any());
    }

    @Test
    void testCreateMatch_withItemPurchasedEvent() {
        var entries = List.of("[00:09:02.522] npc_dota_hero_bloodseeker buys item item_tango");

        when(matchRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());

        var match = createMatchService.createMatch(entries);

        assertThat(match).isNotNull();
        assertThat(match.getCombatLogEntries()).isNotEmpty().hasSize(1);

        var logEntry = match.getCombatLogEntries().stream().findFirst().get();
        assertThat(logEntry).isNotNull();
        assertThat(logEntry.getTimestamp()).isEqualTo(542522000000L);
        assertThat(logEntry.getType()).isEqualTo(CombatLogEntry.Type.ITEM_PURCHASED);
        assertThat(logEntry.getActor()).isEqualTo("npc_dota_hero_bloodseeker");
        assertThat(logEntry.getItem()).isEqualTo("item_tango");

        verify(matchRepository, times(1)).save(any());
        verify(combatLogEntryRepository, times(1)).save(any());
    }

    @Test
    void testCreateMatch_withHeroKilledEvent() {
        var entries = List.of("[00:11:17.489] npc_dota_hero_snapfire is killed by npc_dota_hero_mars");

        when(matchRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());

        var match = createMatchService.createMatch(entries);

        assertThat(match).isNotNull();
        assertThat(match.getCombatLogEntries()).isNotEmpty().hasSize(1);

        var logEntry = match.getCombatLogEntries().stream().findFirst().get();
        assertThat(logEntry).isNotNull();
        assertThat(logEntry.getTimestamp()).isEqualTo(677489000000L);
        assertThat(logEntry.getType()).isEqualTo(CombatLogEntry.Type.HERO_KILLED);
        assertThat(logEntry.getActor()).isEqualTo("npc_dota_hero_mars");
        assertThat(logEntry.getTarget()).isEqualTo("npc_dota_hero_snapfire");

        verify(matchRepository, times(1)).save(any());
        verify(combatLogEntryRepository, times(1)).save(any());
    }

    @Test
    void testCreateMatch_withSpellCastEvent() {
        var entries = List.of(
            "[00:24:26.996] npc_dota_hero_rubick casts ability rubick_spell_steal (lvl 1) on npc_dota_hero_snapfire");

        when(matchRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());

        var match = createMatchService.createMatch(entries);

        assertThat(match).isNotNull();
        assertThat(match.getCombatLogEntries()).isNotEmpty().hasSize(1);

        var logEntry = match.getCombatLogEntries().stream().findFirst().get();
        assertThat(logEntry).isNotNull();
        assertThat(logEntry.getTimestamp()).isEqualTo(1466996000000L);
        assertThat(logEntry.getType()).isEqualTo(CombatLogEntry.Type.SPELL_CAST);
        assertThat(logEntry.getActor()).isEqualTo("npc_dota_hero_rubick");
        assertThat(logEntry.getTarget()).isEqualTo("npc_dota_hero_snapfire");
        assertThat(logEntry.getAbility()).isEqualTo("rubick_spell_steal");
        assertThat(logEntry.getAbilityLevel()).isEqualTo(1);

        verify(matchRepository, times(1)).save(any());
        verify(combatLogEntryRepository, times(1)).save(any());
    }

    @Test
    void testCreateMatch_withDamageEvent() {
        var entries = List.of(
            "[00:25:05.520] npc_dota_hero_pangolier hits npc_dota_hero_death_prophet with pangolier_gyroshell"
            + " for 148 damage (1380->1232)"
        );

        when(matchRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());

        var match = createMatchService.createMatch(entries);

        assertThat(match).isNotNull();
        assertThat(match.getCombatLogEntries()).isNotEmpty().hasSize(1);

        var logEntry = match.getCombatLogEntries().stream().findFirst().get();
        assertThat(logEntry).isNotNull();
        assertThat(logEntry.getTimestamp()).isEqualTo(1505520000000L);
        assertThat(logEntry.getType()).isEqualTo(CombatLogEntry.Type.DAMAGE_DONE);
        assertThat(logEntry.getActor()).isEqualTo("npc_dota_hero_pangolier");
        assertThat(logEntry.getTarget()).isEqualTo("npc_dota_hero_death_prophet");
        assertThat(logEntry.getItem()).isEqualTo("pangolier_gyroshell");
        assertThat(logEntry.getDamage()).isEqualTo(148);

        verify(matchRepository, times(1)).save(any());
        verify(combatLogEntryRepository, times(1)).save(any());
    }

    @Test
    void testCreateMatch_unsupportedEvent() {
        var entries = List.of(
            "[00:37:05.894] npc_dota_hero_death_prophet's death_prophet_exorcism heals npc_dota_hero_death_prophet "
            + "for 68 health (2248->2316)"
        );

        when(matchRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());

        var match = createMatchService.createMatch(entries);

        assertThat(match).isNotNull();
        assertThat(match.getCombatLogEntries()).isEmpty();

        verify(matchRepository, times(1)).save(any());
        verify(combatLogEntryRepository, never()).save(any());
    }

    @Test
    void testCreateMatch_exceptionThrown() {
        var entries = List.of("[00:09:02.522] npc_dota_hero_bloodseeker buys item item_tango");

        when(matchRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());
        when(combatLogEntryRepository.save(any())).thenThrow(RuntimeException.class);

        var match = createMatchService.createMatch(entries);

        verify(matchRepository, times(1)).delete(match);
    }
}