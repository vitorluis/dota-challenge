package gg.bayes.dota_challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DotaChallengeApplication {
    public static void main(String[] args) {
        //        var t = LocalTime.parse("00:37:22.283");
        //        var t = LocalTime.parse("00:01:00.283");
        //        System.out.println(t.get(ChronoField.MILLI_OF_DAY));
        SpringApplication.run(DotaChallengeApplication.class, args);
    }
}
