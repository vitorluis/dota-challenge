package gg.bayes.dota_challenge.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

// Using records for DTO classes (Records are available since JDK 14)
public record HeroDamage(
    String target,

    @JsonProperty("damage_instances")
    int damageInstances,

    @JsonProperty("total_damage")
    int totalDamage
) {
}
