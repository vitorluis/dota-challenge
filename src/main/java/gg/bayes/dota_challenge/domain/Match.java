package gg.bayes.dota_challenge.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "dota_match")
public class Match {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dota_match_sequence_generator")
    @SequenceGenerator(
        name = "dota_match_sequence_generator",
        sequenceName = "dota_match_sequence",
        allocationSize = 1
    )
    @Id
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "match", cascade = CascadeType.PERSIST)
    private Set<CombatLogEntry> combatLogEntries = new HashSet<>();
}