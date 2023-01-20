package gg.bayes.dota_challenge.adapter.repository;

import gg.bayes.dota_challenge.domain.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
}