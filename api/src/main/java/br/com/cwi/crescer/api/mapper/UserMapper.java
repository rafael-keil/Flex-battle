package br.com.cwi.crescer.api.mapper;

import br.com.cwi.crescer.api.domain.User;
import br.com.cwi.crescer.api.security.AuthenticatedUser;

public class UserMapper {

    private UserMapper() {
    }

    public static User toDomain(AuthenticatedUser authenticatedUser) {

        return new User(authenticatedUser.getUsername(), authenticatedUser.getEmail(), authenticatedUser.getName(), 0);
    }
}
