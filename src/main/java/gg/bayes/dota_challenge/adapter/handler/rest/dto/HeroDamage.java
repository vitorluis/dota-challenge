package gg.bayes.dota_challenge.adapter.handler.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

public record HeroDamage(
    String target,

    @JsonProperty("damage_instances")
    Integer damageInstances,

    @JsonProperty("total_damage")
    Integer totalDamage
) {
}
