package gg.bayes.dota_challenge.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Getter
@AllArgsConstructor // Not making it private just to help on tests
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@Table(name = "dota_match")
public final class Match {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dota_match_sequence_generator")
    @SequenceGenerator(
        name = "dota_match_sequence_generator",
        sequenceName = "dota_match_sequence",
        allocationSize = 1
    )
    @Id
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "match")
    private Set<CombatLogEntry> combatLogEntries = new HashSet<>();

    public static Match create(List<String> entries) {
        var match = new Match();

        // As we have timestamp for ordering the events of the match, we can process the data in parallel to help
        // it goes faster.
        // In this case I'm using parallel stream just to "show off". Parallel stream uses thread from the common
        // ForkJoinPool which is shared between the whole application. Ideally the parallel stream should be used
        // only after a benchmark proving that is better than serial stream and if it just makes pure computations,
        // because if is there IO involved (whether disk or network), it will make the thread wait, blocking it to go
        // back to the thread pool
        entries.parallelStream()
            .map(entry -> {
                try {
                    return CombatLogEntry.create(entry, match);
                } catch (UnsupportedEventException e) {
                    log.warn(e.getMessage());
                }
                return null;
            }).filter(Objects::nonNull)
            .forEachOrdered(match::addCombatEntry);

        return match;
    }

    public void addCombatEntry(CombatLogEntry entry) {
        combatLogEntries.add(entry);
    }
}