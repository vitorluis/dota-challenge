package gg.bayes.dota_challenge.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.time.temporal.ChronoField;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@Table(name = "dota_combat_log")
public final class CombatLogEntry {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dota_combat_log_sequence_generator")
    @SequenceGenerator(
        name = "dota_combat_log_sequence_generator",
        sequenceName = "dota_combat_log_sequence",
        allocationSize = 1
    )
    private Long id;

    @ManyToOne(optional = false)
    private Match match;

    @NotNull
    @Column(name = "entry_timestamp")
    private Long timestamp;

    @NotNull
    @Column(name = "entry_type")
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "actor")
    private String actor;

    @Column(name = "target")
    private String target;

    @Column(name = "ability")
    private String ability;

    @Column(name = "ability_level")
    private Integer abilityLevel;

    @Column(name = "item")
    private String item;

    @Column(name = "damage")
    private Integer damage;

    public static CombatLogEntry create(String entry, Match match) {
        // The following are examples of the events we need to process
        // Item purchase: [00:09:02.522] npc_dota_hero_bloodseeker buys item item_tango
        // Kill: [00:11:17.489] npc_dota_hero_snapfire is killed by npc_dota_hero_mars
        // Spell: [00:24:26.996] npc_dota_hero_rubick casts ability rubick_spell_steal (lvl 1) on npc_dota_hero_snapfire
        // Damage: [00:25:05.520] npc_dota_hero_pangolier hits npc_dota_hero_death_prophet with pangolier_gyroshell
        // for 148 damage (1380->1232)

        // Based on the log entry, parse the correct data
        var fields = entry.split(" ");
        if (fields[2].equals("buys")) {
            return createItemPurchaseEntry(fields, match);
        }

        if (fields[2].equals("is") && fields[3].equals("killed")) {
            return createKillEntry(fields, match);
        }

        if (fields[2].equals("casts") && fields[3].equals("ability")) {
            return createSpellCastEntry(fields, match);
        }

        if (fields[2].equals("hits")) {
            return createDamageEntry(fields, match);
        }

        throw new UnsupportedEventException("The event " + entry + " is not supported");
    }

    private static CombatLogEntry createItemPurchaseEntry(String[] fields, Match match) {
        return new CombatLogEntry(
            null,
            match,
            parseTimestamp(fields[0]),
            Type.ITEM_PURCHASED,
            fields[1],
            null,
            null,
            null,
            fields[4],
            null
        );
    }

    private static CombatLogEntry createKillEntry(String[] fields, Match match) {
        // Kill: [00:11:17.489] npc_dota_hero_snapfire is killed by npc_dota_hero_mars
        return new CombatLogEntry(
            null,
            match,
            parseTimestamp(fields[0]),
            Type.HERO_KILLED,
            fields[5],
            fields[1],
            null,
            null,
            null,
            null
        );
    }

    private static CombatLogEntry createSpellCastEntry(String[] fields, Match match) {
        // Spell: [00:24:26.996] npc_dota_hero_rubick casts ability rubick_spell_steal (lvl 1) on npc_dota_hero_snapfire
        return new CombatLogEntry(
            null,
            match,
            parseTimestamp(fields[0]),
            Type.SPELL_CAST,
            fields[1],
            fields[8],
            fields[4],
            Integer.parseInt(fields[6].replace(")", "")),
            null,
            null
        );
    }

    private static CombatLogEntry createDamageEntry(String[] fields, Match match) {
        // Damage: [00:25:05.520] npc_dota_hero_pangolier hits npc_dota_hero_death_prophet with pangolier_gyroshell
        // for 148 damage (1380->1232)
        return new CombatLogEntry(
            null,
            match,
            parseTimestamp(fields[0]),
            Type.DAMAGE_DONE,
            fields[1],
            fields[3],
            null,
            null,
            fields[5],
            Integer.parseInt(fields[7])
        );
    }

    private static long parseTimestamp(String timestamp) {
        // Java does not provide any out-of-the-box to parse [00:09:02.522] as a duration.
        // So one way of parsing it correctly and getting milliseconds is using LocalTime.
        var cleanedTimestamp = timestamp.replace("[", "").replace("]", "");
        return LocalTime.parse(cleanedTimestamp).getLong(ChronoField.NANO_OF_DAY);
    }

    @Override
    public String toString() {
        return "CombatLogEntry{" +
               "id=" + id +
               ", match=" + match +
               ", timestamp=" + timestamp +
               ", type=" + type +
               ", actor='" + actor + '\'' +
               ", target='" + target + '\'' +
               ", ability='" + ability + '\'' +
               ", abilityLevel=" + abilityLevel +
               ", item='" + item + '\'' +
               ", damage=" + damage +
               '}';
    }

    public enum Type {
        ITEM_PURCHASED,
        HERO_KILLED,
        SPELL_CAST,
        DAMAGE_DONE
    }
}
