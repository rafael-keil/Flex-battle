package br.com.cwi.crescer.api.controller;

import br.com.cwi.crescer.api.representation.response.UserLeaderboardResponse;
import br.com.cwi.crescer.api.representation.response.UserResponse;
import br.com.cwi.crescer.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private/user")
public class PrivateUserController {

    @Autowired
    private UserService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUser() {

        return service.getUserResponse();
    }

    @GetMapping("/leaderboard")
    @ResponseStatus(HttpStatus.OK)
    public Page<UserLeaderboardResponse> getLeaderboard() {

        return service.getLeaderboard();
    }
}
