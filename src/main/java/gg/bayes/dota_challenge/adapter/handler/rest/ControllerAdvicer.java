package gg.bayes.dota_challenge.adapter.handler.rest;

import gg.bayes.dota_challenge.adapter.dto.ErrorResponse;
import gg.bayes.dota_challenge.application.MatchNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvicer {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({MatchNotFoundException.class})
    public ErrorResponse handleNotFound(Exception e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }
}
