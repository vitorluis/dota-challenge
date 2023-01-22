package gg.bayes.dota_challenge.application;

import gg.bayes.dota_challenge.adapter.repository.CombatLogEntryRepository;
import gg.bayes.dota_challenge.adapter.repository.MatchRepository;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
            Arguments.of("classpath:combatlog_1.log.txt", 3789),
            Arguments.of("classpath:combatlog_2.log.txt", 3635)
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


        System.out.println(entries);
    }
}