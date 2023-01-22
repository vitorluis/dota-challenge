package gg.bayes.dota_challenge.application;

import gg.bayes.dota_challenge.adapter.repository.CombatLogEntryRepository;
import gg.bayes.dota_challenge.adapter.repository.MatchRepository;
import gg.bayes.dota_challenge.domain.Match;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateMatchService {
    private final MatchRepository matchRepository;
    private final CombatLogEntryRepository combatLogEntryRepository;
    private final PlatformTransactionManager platformTransactionManager;

    /**
     * @see Match#create(List) Please read the explanation about parallel stream
     */
    public Match createMatch(List<String> combatLogEntries) {
        // When using the @Transactional annotation, (together with the cascade declared on Match entity) the commit of
        // the transaction to save the match together with the combat entries was taking ~50 secs.
        // So what I have done here was: Save the combat entries in a manually created transaction, if something
        // fails while saving, it will delete the match. With this, the run time went down to ~30 secs to save all
        // entries.

        // It would be possible to achieve a runtime of ~10 secs, but would need to be done without a transaction to
        // save all entries. The downside of it is that if anything goes wrong, we would need to rollback everything
        // manually
        var match = matchRepository.save(Match.create(combatLogEntries));
        var transaction = new TransactionTemplate(platformTransactionManager);

        return transaction.execute(a -> {
            try {
                match.getCombatLogEntries()
                    .parallelStream()
                    .peek(combatLogEntryRepository::save)
                    .forEachOrdered(System.out::println);

                return match;
            } catch (Exception e) {
                log.error("Error while saving combat entries: {}", e.getMessage());
                matchRepository.delete(match);

                return null;
            }
        });
    }
}
