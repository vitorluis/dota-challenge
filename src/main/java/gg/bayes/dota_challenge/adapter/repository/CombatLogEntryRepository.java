package gg.bayes.dota_challenge.adapter.repository;

import gg.bayes.dota_challenge.domain.CombatLogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CombatLogEntryRepository extends JpaRepository<CombatLogEntry, Long> {
    // TODO: add the necessary methods for your solution
}
