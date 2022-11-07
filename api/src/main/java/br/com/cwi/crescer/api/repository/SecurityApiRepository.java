package br.com.cwi.crescer.api.repository;

import br.com.cwi.crescer.api.security.AuthenticatedUser;

public interface SecurityApiRepository {

    String loginUser(String username, String password);

    AuthenticatedUser getUser(String token);
}
