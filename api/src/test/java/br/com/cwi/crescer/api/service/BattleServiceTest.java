package br.com.cwi.crescer.api.service;

import br.com.cwi.crescer.api.domain.Battle;
import br.com.cwi.crescer.api.domain.User;
import br.com.cwi.crescer.api.exception.BadRequestException;
import br.com.cwi.crescer.api.exception.ForbiddenException;
import br.com.cwi.crescer.api.fixture.BattleFixture;
import br.com.cwi.crescer.api.fixture.ChallengeFixture;
import br.com.cwi.crescer.api.fixture.UserFixture;
import br.com.cwi.crescer.api.repository.BattleRepository;
import br.com.cwi.crescer.api.representation.request.InputRequest;
import br.com.cwi.crescer.api.representation.response.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BattleServiceTest {

    @InjectMocks
    @Spy
    private BattleService tested;

    @Mock
    private BattleRepository repository;

    @Mock
    private SecurityApiService securityApiService;

    @Mock
    private SimpMessagingTemplate template;

    @Mock
    private ChallengeService challengeService;

    @Mock
    private UserService userService;

    @Captor
    private ArgumentCaptor<Battle> battleArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    ModelMapper modelMapper = new ModelMapper();

    @Test
    public void quandoChamarGetNewBattleTokenDeveCriarBatalhaERetornarToken() {

        User user = UserFixture.user();

        when(userService.getUser())
                .thenReturn(user);
        when(repository.findByToken(any(Integer.class)))
                .thenReturn(null);

        String tokenReturned = tested.getNewBattleToken();

        verify(userService).getUser();
        verify(repository).findByToken(any(Integer.class));
        verify(repository).save(battleArgumentCaptor.capture());
        assertEquals(user, battleArgumentCaptor.getValue().getOwner());
        assertEquals(battleArgumentCaptor.getValue().getToken().toString(), tokenReturned);
    }

    @Test
    public void quandoChamarGetNewBattleTokenEJaExistirTokenDeveCriarCriarOutroToken() {

        User user = UserFixture.user();
        Battle battle = BattleFixture.battle();

        when(userService.getUser())
                .thenReturn(user);
        when(repository.findByToken(any(Integer.class)))
                .thenReturn(battle)
                .thenReturn(battle)
                .thenReturn(battle)
                .thenReturn(null);

        String tokenReturned = tested.getNewBattleToken();

        verify(userService).getUser();
        verify(repository, atLeast(4)).findByToken(any(Integer.class));
        verify(repository).save(battleArgumentCaptor.capture());
        assertEquals(user, battleArgumentCaptor.getValue().getOwner());
        assertEquals(battleArgumentCaptor.getValue().getToken().toString(), tokenReturned);
    }

    @Test
    public void quandoChamarEnterBattleDeveSalvarUsuarioComoOpponent() {

        Battle battle = BattleFixture.battle();
        String token = battle.getToken().toString();
        User user = UserFixture.userAlternative();

        doReturn(battle)
                .when(tested).findBattleByToken(token);
        when(userService.getUser())
                .thenReturn(user);

        tested.enterBattle(token);

        verify(tested).findBattleByToken(token);
        verify(userService).getUser();
        battle.setOpponent(user);
        verify(repository).save(battle);
    }

    @Test(expected = ForbiddenException.class)
    public void quandoChamarEnterBattleComOpponentDeveRetornarException() {

        Battle battle = BattleFixture.battle();
        String token = battle.getToken().toString();
        User user = UserFixture.user();

        doReturn(battle)
                .when(tested).findBattleByToken(token);
        when(userService.getUser())
                .thenReturn(user);

        tested.enterBattle(token);
    }

    @Test(expected = ForbiddenException.class)
    public void quandoChamarEnterBattleSendoOOwnerDeveRetornarException() {

        Battle battle = BattleFixture.battle();
        String token = battle.getToken().toString();
        User user = UserFixture.userAlternative();
        battle.setOpponent(user);

        doReturn(battle)
                .when(tested).findBattleByToken(token);

        tested.enterBattle(token);
    }

    @Test
    public void quandoChamarGetStartBattleChallengesDeveMandarPayloadEProximasBatalhasETempoFinal() {

        Battle battle = BattleFixture.battle();
        String token = battle.getToken().toString();
        String topic = "/room/" + battle.getToken().toString();
        Page<ChallengeResponse> challengeResponsePage = ChallengeFixture.challengeResponsePageWithTwo();
        PayloadBattleResponse payloadBattleResponse = BattleFixture.payloadBattleResponse();

        when(challengeService.getRandomChallengePage())
                .thenReturn(challengeResponsePage);
        doNothing()
                .when(tested).getBattleUsers(token);
        doNothing()
                .when(tested).getEndBattleTime(token);
        doNothing()
                .when(tested).getNewOwnerChallenge(token);
        doNothing()
                .when(tested).getNewOpponentChallenge(token);

        tested.getStartBattleChallenges(token);

        verify(challengeService).getRandomChallengePage();
        verify(template).convertAndSend(topic, payloadBattleResponse);
        verify(tested).getBattleUsers(token);
        verify(tested).getEndBattleTime(token);
        verify(tested).getNewOwnerChallenge(token);
        verify(tested).getNewOpponentChallenge(token);
    }

    @Test
    public void quandoChamarInputOwnerBattleDevemandarInput() {

        InputRequest inputRequest = BattleFixture.completeInputRequest();
        Battle battle = BattleFixture.battle();
        String token = battle.getToken().toString();
        String topic = "/room/" + token + "/owner/input";

        tested.inputOwnerBattle(inputRequest, token);

        verify(template).convertAndSend(topic, inputRequest);
    }

    @Test
    public void quandoChamarInputOpponentBattleDevemandarInput() {

        InputRequest inputRequest = BattleFixture.completeInputRequest();
        Battle battle = BattleFixture.battle();
        String token = battle.getToken().toString();
        String topic = "/room/" + token + "/opponent/input";

        tested.inputOpponentBattle(inputRequest, token);

        verify(template).convertAndSend(topic, inputRequest);
    }

    @Test
    public void quandoChamarGetBattleUsersDeveMandarBattleUserResponse() {

        Battle battle = BattleFixture.battle();
        String token = battle.getToken().toString();
        String topic = "/room/" + token + "/users";
        BattleUsersResponse battleUsersResponse = modelMapper.map(battle, BattleUsersResponse.class);

        doReturn(battle)
                .when(tested).findBattleByToken(token);

        tested.getBattleUsers(token);

        verify(tested).findBattleByToken(token);
        verify(template).convertAndSend(topic, battleUsersResponse);
    }

    @Test
    public void quandoChamarGetEndBattleTimeDeveSalvarTempoFinalEMandarTempo() {

        Battle battle = BattleFixture.battle();
        String token = battle.getToken().toString();
        Date currentDate = new Date();
        Long time = currentDate.getTime();
        Long endTime = time + 125900;

        doReturn(battle)
                .when(tested).findBattleByToken(token);

        tested.getEndBattleTime(token);

        verify(tested).findBattleByToken(token);
        verify(repository).save(battleArgumentCaptor.capture());
        assertEquals(endTime, battleArgumentCaptor.getValue().getEndTime(), 1);
    }

    @Test
    public void quandoChamarGetNewOwnerChallengeDeveMandarNewChallenge() {

        Battle battle = BattleFixture.battle();
        String token = battle.getToken().toString();
        String topic = "/room/" + token + "/owner/challenge";
        ChallengeResponse challengeResponse = ChallengeFixture.challengeResponse();
        ChallengeBattleResponse challengeBattleResponse = ChallengeFixture.challengeBattleResponse();

        doReturn(battle)
                .when(tested).findBattleByToken(token);
        doReturn(false)
                .when(tested).isBatalhaFinished(battle);
        when(challengeService.getRandomChallenge())
                .thenReturn(challengeResponse);
        doNothing()
                .when(tested).updateOwnerPoints(token);

        tested.getNewOwnerChallenge(token);

        verify(tested).findBattleByToken(token);
        verify(tested).isBatalhaFinished(battle);
        verify(challengeService).getRandomChallenge();
        verify(template).convertAndSend(topic, challengeBattleResponse);
        verify(tested).updateOwnerPoints(token);
    }

    @Test(expected = ForbiddenException.class)
    public void quandoChamarGetNewOwnerChallengeFinalizadaDeveRetornarException() {

        Battle battle = BattleFixture.battle();
        String token = battle.getToken().toString();

        doReturn(battle)
                .when(tested).findBattleByToken(token);
        doReturn(true)
                .when(tested).isBatalhaFinished(battle);

        tested.getNewOwnerChallenge(token);
        verify(tested).findBattleByToken(token);
    }

    @Test
    public void quandoChamarGetNewOpponentChallengeDeveMandarNewChallenge() {

        Battle battle = BattleFixture.battle();
        String token = battle.getToken().toString();
        String topic = "/room/" + token + "/opponent/challenge";
        ChallengeResponse challengeResponse = ChallengeFixture.challengeResponse();
        ChallengeBattleResponse challengeBattleResponse = ChallengeFixture.challengeBattleResponse();

        doReturn(battle)
                .when(tested).findBattleByToken(token);
        doReturn(false)
                .when(tested).isBatalhaFinished(battle);
        when(challengeService.getRandomChallenge())
                .thenReturn(challengeResponse);
        doNothing()
                .when(tested).updateOpponentPoints(token);

        tested.getNewOpponentChallenge(token);

        verify(tested).findBattleByToken(token);
        verify(tested).isBatalhaFinished(battle);
        verify(challengeService).getRandomChallenge();
        verify(template).convertAndSend(topic, challengeBattleResponse);
        verify(tested).updateOpponentPoints(token);
    }

    @Test(expected = ForbiddenException.class)
    public void quandoChamarGetNewOpponentChallengeDeveRetornarException() {

        Battle battle = BattleFixture.battle();
        String token = battle.getToken().toString();

        doReturn(battle)
                .when(tested).findBattleByToken(token);
        doReturn(true)
                .when(tested).isBatalhaFinished(battle);

        tested.getNewOpponentChallenge(token);

        verify(tested).findBattleByToken(token);
    }

    @Test
    public void quandoChamarUpdateOwnerPointsEmBattleSemPontosDeveColocarZeroEmOwnerPoints() {

        Battle battle = BattleFixture.battle();
        String token = battle.getToken().toString();

        doReturn(battle)
                .when(tested).findBattleByToken(token);

        tested.updateOwnerPoints(token);

        verify(tested).findBattleByToken(token);
        verify(repository).save(battleArgumentCaptor.capture());
        assertEquals(0, battleArgumentCaptor.getValue().getOwnerPoints().intValue());
    }

    @Test
    public void quandoChamarUpdateOwnerPointsEmBattleComPontosDeveAdicionarUmEmOwnerPoints() {

        Battle battle = BattleFixture.battleWithPoints();
        String token = battle.getToken().toString();

        doReturn(battle)
                .when(tested).findBattleByToken(token);

        tested.updateOwnerPoints(token);

        verify(tested).findBattleByToken(token);
        verify(repository).save(battleArgumentCaptor.capture());
        assertEquals(2, battleArgumentCaptor.getValue().getOwnerPoints().intValue());
    }

    @Test
    public void quandoChamarUpdateOpponentPointsEmBattleSemPontosDeveColocarZeroEmOpponentPoints() {

        Battle battle = BattleFixture.battle();
        String token = battle.getToken().toString();

        doReturn(battle)
                .when(tested).findBattleByToken(token);

        tested.updateOpponentPoints(token);

        verify(tested).findBattleByToken(token);
        verify(repository).save(battleArgumentCaptor.capture());
        assertEquals(0, battleArgumentCaptor.getValue().getOpponentPoints().intValue());
    }

    @Test
    public void quandoChamarUpdateOpponentPointsEmBattleComPontosDeveAdicionarUmEmOpponentPoints() {

        Battle battle = BattleFixture.battleWithPoints();
        String token = battle.getToken().toString();

        doReturn(battle)
                .when(tested).findBattleByToken(token);

        tested.updateOpponentPoints(token);

        verify(tested).findBattleByToken(token);
        verify(repository).save(battleArgumentCaptor.capture());
        assertEquals(2, battleArgumentCaptor.getValue().getOpponentPoints().intValue());
    }

    @Test
    public void quandoChamarGetWinnerDeveComputarRanking() {

        Battle battle = BattleFixture.battleWithPoints();
        String token = battle.getToken().toString();
        String topic = "/room/" + token + "/winner";

        doReturn(battle)
                .when(tested).findBattleByToken(token);
        doReturn(true)
                .when(tested).isBatalhaFinished(battle);
        doNothing()
                .when(tested).computateRanking(battle, topic);

        tested.getWinner(token);

        verify(tested).findBattleByToken(token);
        verify(tested).isBatalhaFinished(battle);
        verify(repository).save(battleArgumentCaptor.capture());
        assertTrue(battleArgumentCaptor.getValue().getComputed());
        verify(tested).computateRanking(battle, topic);
    }

    @Test(expected = ForbiddenException.class)
    public void quandoChamarGetWinnerComputedDeveRetornarException() {

        Battle battle = BattleFixture.battleWithPoints();
        battle.setComputed(true);
        String token = battle.getToken().toString();

        doReturn(battle)
                .when(tested).findBattleByToken(token);

        tested.getWinner(token);

        verify(tested).findBattleByToken(token);
    }

    @Test(expected = ForbiddenException.class)
    public void quandoChamarGetWinnerComBattleNaoFinalizadaDeveRetornarException() {

        Battle battle = BattleFixture.battleWithPoints();
        String token = battle.getToken().toString();

        doReturn(battle)
                .when(tested).findBattleByToken(token);
        doReturn(false)
                .when(tested).isBatalhaFinished(battle);

        tested.getWinner(token);

        verify(tested).findBattleByToken(token);
        verify(tested).isBatalhaFinished(battle);
    }

    @Test
    public void quandoChamarComputateRankingComOwnerGanhandoDeveMandarResponse() {

        Battle battle = BattleFixture.battleWithPoints();
        battle.setOwnerPoints(2);
        String token = battle.getToken().toString();
        String topic = "/room/" + token + "/winner";
        Integer delta = 5;
        Integer ownerRanking = 30;
        Integer opponentRanking = -20;
        BattleRankingResponse battleRankingResponse = BattleFixture.battleRankingResponse();

        doReturn(delta)
                .when(tested).calculateOwnerDelta(battle);
        doNothing()
                .when(userService).updateUserRanking(battle.getOwner(), ownerRanking);
        doNothing()
                .when(userService).updateUserRanking(battle.getOpponent(), opponentRanking);

        tested.computateRanking(battle, topic);

        verify(tested).calculateOwnerDelta(battle);
        verify(userService).updateUserRanking(battle.getOwner(), ownerRanking);
        verify(userService).updateUserRanking(battle.getOpponent(), opponentRanking);
        verify(template).convertAndSend(topic, battleRankingResponse);
    }

    @Test
    public void quandoChamarComputateRankingComOpponentGanhandoDeveMandarResponse() {

        Battle battle = BattleFixture.battleWithPoints();
        battle.setOpponentPoints(2);
        String token = battle.getToken().toString();
        String topic = "/room/" + token + "/winner";
        Integer delta = 5;
        Integer ownerRanking = -20;
        Integer opponentRanking = 30;
        BattleRankingResponse battleRankingResponse = BattleFixture.battleRankingResponseAlternative();

        doReturn(delta)
                .when(tested).calculateOpponentDelta(battle);
        doNothing()
                .when(userService).updateUserRanking(battle.getOwner(), ownerRanking);
        doNothing()
                .when(userService).updateUserRanking(battle.getOpponent(), opponentRanking);

        tested.computateRanking(battle, topic);

        verify(tested).calculateOpponentDelta(battle);
        verify(userService).updateUserRanking(battle.getOwner(), ownerRanking);
        verify(userService).updateUserRanking(battle.getOpponent(), opponentRanking);
        verify(template).convertAndSend(topic, battleRankingResponse);
    }

    @Test
    public void quandoChamarIsBattleFinishedDeveRetornarTrue() {

        Battle battle = BattleFixture.battle();
        battle.setEndTime(1529644667834L);

        Boolean batalhaFinished = tested.isBatalhaFinished(battle);

        assertTrue(batalhaFinished);
    }

    @Test
    public void quandoChamarIsBattleFinishedDeveRetornarFalse() {

        Battle battle = BattleFixture.battle();
        battle.setEndTime(15296446678340L);

        Boolean batalhaFinished = tested.isBatalhaFinished(battle);

        assertFalse(batalhaFinished);
    }

    @Test
    public void quandoChamarFindBattleByTokenDeveRetornarBattle() {

        Battle battle = BattleFixture.battle();
        Integer token = battle.getToken();

        when(repository.findByToken(token))
                .thenReturn(battle);

        Battle battleReturned = tested.findBattleByToken(token.toString());

        verify(repository).findByToken(token);
        assertEquals(battle, battleReturned);
    }

    @Test(expected = BadRequestException.class)
    public void quandoChamarFindBattleByTokenInvalidoDeveRetornarException() {

        String token = "invalid";

        tested.findBattleByToken(token);
    }

    @Test
    public void quandoChamarConstructTopicDeveRetornarTopic() {

        String room = "12345";
        String endUrl = "/winner";
        String topic = "/room/12345/winner";

        String topicReturned = tested.constructTopic(room, endUrl);

        assertEquals(topic, topicReturned);
    }

    @Test
    public void quandoChamarCalculateOwnerDeltaComMenosRankingDeveRetornarDeltaMultiplicado() {

        Battle battle = BattleFixture.battleWithPoints();
        battle.setOwnerPoints(5);
        Integer delta = 7;

        Integer deltaReturned = tested.calculateOwnerDelta(battle);

        assertEquals(delta, deltaReturned);
    }

    @Test
    public void quandoChamarCalculateOwnerDeltaComMaisRankingDeveRetornarDeltaDividido() {

        Battle battle = BattleFixture.BattleWithPointsReverse();
        battle.setOwnerPoints(6);
        Integer delta = -3;

        Integer deltaReturned = tested.calculateOwnerDelta(battle);

        assertEquals(delta, deltaReturned);
    }

    @Test
    public void quandoChamarCalculateOpponentDeltaComMenosRankingDeveRetornarDeltaMultiplicado() {

        Battle battle = BattleFixture.BattleWithPointsReverse();
        battle.setOpponentPoints(5);
        Integer delta = 7;

        Integer deltaReturned = tested.calculateOpponentDelta(battle);

        assertEquals(delta, deltaReturned);
    }

    @Test
    public void quandoChamarCalculateOpponentDeltaComMaisRankingDeveRetornarDeltaDividido() {

        Battle battle = BattleFixture.battleWithPoints();
        battle.setOpponentPoints(6);
        Integer delta = -3;

        Integer deltaReturned = tested.calculateOpponentDelta(battle);

        assertEquals(delta, deltaReturned);
    }
}