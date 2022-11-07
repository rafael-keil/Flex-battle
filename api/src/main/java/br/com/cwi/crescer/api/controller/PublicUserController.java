package br.com.cwi.crescer.api.controller;

import br.com.cwi.crescer.api.representation.request.LoginUserRequest;
import br.com.cwi.crescer.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/user")
public class PublicUserController {

    @Autowired
    private UserService service;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String loginUser(@RequestBody LoginUserRequest request) {

        return service.loginUser(request);
    }
}
