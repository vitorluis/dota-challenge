package gg.bayes.dota_challenge.adapter.repository;

import gg.bayes.dota_challenge.adapter.dto.HeroKills;
import gg.bayes.dota_challenge.domain.CombatLogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CombatLogEntryRepository extends JpaRepository<CombatLogEntry, Long> {

    @Query("""
            SELECT new gg.bayes.dota_challenge.adapter.dto.HeroKills(cle.actor, count(cle))
            FROM CombatLogEntry cle
            where cle.type = 'HERO_KILLED' and cle.match.id = :matchId
            group by cle.actor
        """)
    List<HeroKills> findAllKillsByMatchId(Long matchId);
}
