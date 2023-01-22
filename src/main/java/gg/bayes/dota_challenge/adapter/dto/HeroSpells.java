package gg.bayes.dota_challenge.adapter.dto;

// Using records for DTO classes (Records are available since JDK 14)
public record HeroSpells(String spell, long casts) {
}
