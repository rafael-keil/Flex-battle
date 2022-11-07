package br.com.cwi.crescer.api.mapper;

import br.com.cwi.crescer.api.domain.User;
import br.com.cwi.crescer.api.fixture.UserFixture;
import br.com.cwi.crescer.api.security.AuthenticatedUser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserMapperTest {

    @Test
    public void quandoInformarAuthenticatedUserDeveRetornarUmUser() {

        User user = UserFixture.user();
        AuthenticatedUser authenticatedUser = UserFixture.authenticatedUser();

        User userReturned = UserMapper.toDomain(authenticatedUser);

        assertEquals(user.getUsername(), userReturned.getUsername());
        assertEquals(user.getName(), userReturned.getName());
        assertEquals(user.getEmail(), userReturned.getEmail());
        assertEquals(user.getRanking(), userReturned.getRanking());
    }

}