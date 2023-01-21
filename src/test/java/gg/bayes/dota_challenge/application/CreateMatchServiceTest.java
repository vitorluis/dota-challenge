package gg.bayes.dota_challenge.application;

import gg.bayes.dota_challenge.domain.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class CreateMatchServiceTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreate() throws IOException {
        var file = ResourceUtils.getFile("classpath:combatlog_1.log.txt");
        var content = new HashSet<>(Files.readAllLines(file.toPath()));

        var m = Match.create(content);


        System.out.println(content);
    }
}