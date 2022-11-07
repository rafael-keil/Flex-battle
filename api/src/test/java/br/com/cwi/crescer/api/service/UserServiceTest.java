package br.com.cwi.crescer.api.service;

import br.com.cwi.crescer.api.domain.User;
import br.com.cwi.crescer.api.exception.NotFoundException;
import br.com.cwi.crescer.api.fixture.UserFixture;
import br.com.cwi.crescer.api.repository.UserRepository;
import br.com.cwi.crescer.api.representation.request.LoginUserRequest;
import br.com.cwi.crescer.api.representation.response.UserLeaderboardResponse;
import br.com.cwi.crescer.api.representation.response.UserResponse;
import br.com.cwi.crescer.api.security.AuthenticatedUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    @Spy
    private UserService tested;

    @Mock
    private UserRepository repository;

    @Mock
    private SecurityApiService securityApiService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    public void quandoChamarLoginUserDeveRetornarToken() {

        User user = UserFixture.user();
        LoginUserRequest loginUserRequest = UserFixture.loginUserRequest();
        String token = "token";

        when(securityApiService.loginUser(loginUserRequest.getUsername(), loginUserRequest.getPassword()))
                .thenReturn(token);
        when(repository.findByUsername(loginUserRequest.getUsername()))
                .thenReturn(user);

        String tokenReturned = tested.loginUser(loginUserRequest);

        verify(securityApiService).loginUser(loginUserRequest.getUsername(), loginUserRequest.getPassword());
        verify(repository).findByUsername(loginUserRequest.getUsername());
        assertEquals(token, tokenReturned);
    }

    @Test
    public void quandoChamarLoginUserNaoCadastradoDeveRetornarCadastrar() {

        User user = UserFixture.user();
        AuthenticatedUser authenticatedUser = UserFixture.authenticatedUser();
        LoginUserRequest loginUserRequest = UserFixture.loginUserRequest();
        String token = "token";

        when(securityApiService.loginUser(loginUserRequest.getUsername(), loginUserRequest.getPassword()))
                .thenReturn(token);
        when(repository.findByUsername(loginUserRequest.getUsername()))
                .thenReturn(null);
        when(securityApiService.getAuthenticatedUserByToken(token))
                .thenReturn(authenticatedUser);

        tested.loginUser(loginUserRequest);

        verify(repository).save(user);
        verify(securityApiService).getAuthenticatedUserByToken(token);
    }

    @Test(expected = NotFoundException.class)
    public void quandoChamarLoginUserInvalidoDeveRetornarException() {

        LoginUserRequest loginUserRequest = UserFixture.loginUserRequest();

        when(securityApiService.loginUser(loginUserRequest.getUsername(), loginUserRequest.getPassword()))
                .thenThrow(UsernameNotFoundException.class);

        tested.loginUser(loginUserRequest);
    }

    @Test
    public void quandoChamarUpdateUserRankingDeveAtualizarUserRanking() {
        User user = UserFixture.user();
        Integer ranking = 25;
        Integer atualizedRanking = user.getRanking() + ranking;

        tested.updateUserRanking(user, ranking);

        verify(repository).save(userArgumentCaptor.capture());
        assertEquals(atualizedRanking, userArgumentCaptor.getValue().getRanking());
    }

    @Test
    public void quandoChamarUpdateUserRankingQueFicariaNegativoDeveManterZero() {
        User user = UserFixture.user();
        Integer ranking = -(user.getRanking() + 25);
        Integer atualizedRanking = 0;

        tested.updateUserRanking(user, ranking);

        verify(repository).save(userArgumentCaptor.capture());
        assertEquals(atualizedRanking, userArgumentCaptor.getValue().getRanking());
    }

    @Test
    public void quandoChamarGetUserDeveRetornarUser() {

        AuthenticatedUser authenticatedUser = UserFixture.authenticatedUser();
        User user = UserFixture.user();

        when(securityApiService.getAuthenticatedUser())
                .thenReturn(authenticatedUser);
        when(repository.findByUsername(authenticatedUser.getUsername()))
                .thenReturn(user);

        User userReturned = tested.getUser();

        verify(securityApiService).getAuthenticatedUser();
        verify(repository).findByUsername(authenticatedUser.getUsername());
        assertEquals(user, userReturned);
    }

    @Test
    public void quandoChamarGetUserResponseDeveRetornarUserResponse() {

        UserResponse userResponse = UserFixture.userResponse();
        User user = UserFixture.user();

        doReturn(user)
                .when(tested).getUser();

        UserResponse userResponseReturned = tested.getUserResponse();

        verify(tested).getUser();
        assertEquals(userResponse, userResponseReturned);
    }


    @Test
    public void quandoChamarGetLeaderboard() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = UserFixture.userPage();
        Page<UserLeaderboardResponse> userLeaderboardResponsePage = UserFixture.userLeaderboardResponsePage();

        when(repository.getLeaderboard(pageable))
                .thenReturn(userPage);

        Page<UserLeaderboardResponse> leaderboardPageReturned = tested.getLeaderboard();

        verify(repository).getLeaderboard(pageable);
        assertEquals(userLeaderboardResponsePage, leaderboardPageReturned);
    }
}