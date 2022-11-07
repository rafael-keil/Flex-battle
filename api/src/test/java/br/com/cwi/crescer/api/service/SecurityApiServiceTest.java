package br.com.cwi.crescer.api.service;

import br.com.cwi.crescer.api.fixture.UserFixture;
import br.com.cwi.crescer.api.repository.SecurityApiRepository;
import br.com.cwi.crescer.api.security.AuthenticatedUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SecurityApiServiceTest {

    @InjectMocks
    private SecurityApiService tested;

    @Mock
    private SecurityApiRepository repository;

    @Test
    public void quandoChamarGetAuthenticatedUserDeveRetornarToken() {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        AuthenticatedUser authenticatedUser = UserFixture.authenticatedUser();
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);
        when(tested.getAuthenticatedUser())
                .thenReturn(authenticatedUser);

        AuthenticatedUser authenticatedUserReturned = tested.getAuthenticatedUser();

        verify(securityContext, atLeast(1)).getAuthentication();

        assertEquals(authenticatedUser, authenticatedUserReturned);
    }

    @Test
    public void quandoChamarLoginUserDeveRetornarToken() {

        String username = "username";
        String password = "password";
        String token = "token";

        when(repository.loginUser(username, password))
                .thenReturn(token);

        tested.loginUser(username, password);

        verify(repository).loginUser(username, password);
    }

    @Test
    public void quandoChamarGetAuthenticatedUserByTokenDeveRetornarAuthenticatedUser() {

        AuthenticatedUser authenticatedUser = UserFixture.authenticatedUser();
        String token = "token";

        when(repository.getUser(token))
                .thenReturn(authenticatedUser);

        tested.getAuthenticatedUserByToken(token);

        verify(repository).getUser(token);
    }

}