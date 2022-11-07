package br.com.cwi.crescer.api.service;

import br.com.cwi.crescer.api.repository.SecurityApiRepository;
import br.com.cwi.crescer.api.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityApiService {

    @Autowired
    private SecurityApiRepository repository;

    public AuthenticatedUser getAuthenticatedUser() {
        return (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public String loginUser(String username, String password) {
        return repository.loginUser(username, password);
    }

    public AuthenticatedUser getAuthenticatedUserByToken(String token) {
        return repository.getUser(token);
    }
}
