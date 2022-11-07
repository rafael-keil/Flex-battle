package br.com.cwi.crescer.api.controller;

import br.com.cwi.crescer.api.representation.request.InputRequest;
import br.com.cwi.crescer.api.service.BattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/private/battle")
public class BattleController {

    @Autowired
    private BattleService service;

    @Autowired
    private SimpMessagingTemplate template;

    @GetMapping("/token")
    @ResponseStatus(HttpStatus.CREATED)
    public String getNewBattleToken() {

        return service.getNewBattleToken();
    }

    @PutMapping("/{room}/enter")
    @ResponseStatus(HttpStatus.OK)
    public void enterBattle(@PathVariable String room) {

        service.enterBattle(room);
    }

    @GetMapping("/{room}/start")
    @ResponseStatus(HttpStatus.OK)
    public void getStartBattleChallenges(@PathVariable String room) {

        service.getStartBattleChallenges(room);
    }

    @PostMapping("/{room}/owner/input")
    @ResponseStatus(HttpStatus.OK)
    public void inputOwnerBattle(@RequestBody InputRequest request, @PathVariable String room) {

        service.inputOwnerBattle(request, room);
    }

    @PostMapping("/{room}/opponent/input")
    @ResponseStatus(HttpStatus.OK)
    public void inputOpponentBattle(@RequestBody InputRequest request, @PathVariable String room) {
        service.inputOpponentBattle(request, room);
    }

    @GetMapping("/{room}/owner/challenge")
    @ResponseStatus(HttpStatus.OK)
    public void getNewOwnerChallenge(@PathVariable String room) {

        service.getNewOwnerChallenge(room);
    }

    @GetMapping("/{room}/opponent/challenge")
    @ResponseStatus(HttpStatus.OK)
    public void getNewOpponentChallenge(@PathVariable String room) {

        service.getNewOpponentChallenge(room);
    }

    @GetMapping("/{room}/winner")
    @ResponseStatus(HttpStatus.OK)
    public void getWinner(@PathVariable String room) {

        service.getWinner(room);
    }
}
