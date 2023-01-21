package gg.bayes.dota_challenge.adapter.handler.rest.dto;

// Using records for DTO classes (Records are available since JDK 14)
public record HeroSpells(String spell, int casts) {
}
