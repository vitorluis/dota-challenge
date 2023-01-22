package gg.bayes.dota_challenge.application;

public class HeroNotFoundException extends RuntimeException {
    public HeroNotFoundException(String message) {
        super(message);
    }
}
