package gg.bayes.dota_challenge.adapter.repository;

import gg.bayes.dota_challenge.adapter.dto.HeroItem;
import gg.bayes.dota_challenge.adapter.dto.HeroKills;
import gg.bayes.dota_challenge.adapter.dto.HeroSpells;
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
            WHERE cle.type = 'HERO_KILLED' AND cle.match.id = :matchId
            GROUP BY cle.actor
        """)
    List<HeroKills> findAllKillsByMatchId(long matchId);

    @Query("""
        SELECT new gg.bayes.dota_challenge.adapter.dto.HeroItem(cle.item, cle.timestamp)
        FROM CombatLogEntry cle
        WHERE cle.match.id = :matchId AND cle.actor = :hero and cle.type = 'ITEM_PURCHASED'
        ORDER BY cle.timestamp
        """)
    List<HeroItem> findAllHeroItems(long matchId, String hero);

    @Query("""
            SELECT new gg.bayes.dota_challenge.adapter.dto.HeroSpells(cle.ability, count(cle))
            FROM CombatLogEntry cle
            WHERE cle.type = 'SPELL_CAST' AND cle.match.id = :matchId AND cle.actor = :hero
            GROUP BY cle.ability
        """)
    List<HeroSpells> findAllHeroSpells(long matchId, String hero);
}
