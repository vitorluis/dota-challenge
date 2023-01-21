package gg.bayes.dota_challenge.adapter.handler.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

// Using records for DTO classes (Records are available since JDK 14)
public record HeroDamage(
    String target,

    @JsonProperty("damage_instances")
    int damageInstances,

    @JsonProperty("total_damage")
    int totalDamage
) {
}
