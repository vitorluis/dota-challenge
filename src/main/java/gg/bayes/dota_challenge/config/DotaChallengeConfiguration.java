package gg.bayes.dota_challenge.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
    title = "Dota Challenge API",
    version = "1.0",
    description = "Documentation Dota Challenge API")
)
public class DotaChallengeConfiguration {
}
