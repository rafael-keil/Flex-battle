package br.com.cwi.crescer.api.controller;

import br.com.cwi.crescer.api.representation.response.ChallengeResponse;
import br.com.cwi.crescer.api.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private/challenge")
public class ChallengeController {

    @Autowired
    private ChallengeService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ChallengeResponse getRandomChallenge() {

        return service.getRandomChallenge();
    }
}
