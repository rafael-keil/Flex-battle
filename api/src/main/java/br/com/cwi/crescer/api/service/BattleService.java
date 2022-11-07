package br.com.cwi.crescer.api.service;

import br.com.cwi.crescer.api.domain.Battle;
import br.com.cwi.crescer.api.domain.User;
import br.com.cwi.crescer.api.exception.BadRequestException;
import br.com.cwi.crescer.api.exception.ForbiddenException;
import br.com.cwi.crescer.api.exception.NotFoundException;
import br.com.cwi.crescer.api.mapper.BattleMapper;
import br.com.cwi.crescer.api.repository.BattleRepository;
import br.com.cwi.crescer.api.representation.request.InputRequest;
import br.com.cwi.crescer.api.representation.response.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

import static java.util.Optional.ofNullable;

@Service
public class BattleService {

    @Autowired
    private BattleRepository repository;

    @Autowired
    private SecurityApiService securityApiService;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private UserService userService;

    ModelMapper modelMapper = new ModelMapper();

    Random random = new Random();

    public String getNewBattleToken() {

        User user = userService.getUser();
        Integer token;

        do {
            token = random.nextInt(899999) + 100000;
        } while (repository.findByToken(token) != null);

        Battle battle = BattleMapper.toDomain(token, user);

        repository.save(battle);

        return battle.getToken().toString();
    }

    public void enterBattle(String room) {

        Battle battle = findBattleByToken(room);

        if (battle.getOpponent() != null) {
            throw new ForbiddenException("Oponente já alocado.");
        }

        User user = userService.getUser();

        if (battle.getOwner().getUsername().equals(user.getUsername())) {
            throw new ForbiddenException("Oponente não pode ser igual ao dono da sala.");
        }

        battle.setOpponent(user);
        repository.save(battle);
    }

    public void getStartBattleChallenges(String room) {

        String topic = constructTopic(room, "");

        Page<ChallengeResponse> randomChelengePage = challengeService.getRandomChallengePage();
        PayloadBattleResponse payloadBattleResponse = BattleMapper.toPayloadBattleResponse(randomChelengePage);

        template.convertAndSend(topic, payloadBattleResponse);

        getEndBattleTime(room);
        getBattleUsers(room);
        getNewOwnerChallenge(room);
        getNewOpponentChallenge(room);
    }

    public void inputOwnerBattle(InputRequest request, String room) {

        String topic = constructTopic(room, "/owner/input");

        template.convertAndSend(topic, request);
    }

    public void inputOpponentBattle(InputRequest request, String room) {

        String topic = constructTopic(room, "/opponent/input");

        template.convertAndSend(topic, request);
    }

    public void getBattleUsers(String room) {

        String topic = constructTopic(room, "/users");
        Battle battle = findBattleByToken(room);

        BattleUsersResponse battleUsersResponse = modelMapper.map(battle, BattleUsersResponse.class);

        template.convertAndSend(topic, battleUsersResponse);
    }

    public void getEndBattleTime(String room) {

        Date currentDate = new Date();
        Long time = currentDate.getTime();
        Long endTime = time + 125900;

        Battle battle = findBattleByToken(room);
        battle.setEndTime(endTime);

        repository.save(battle);
    }

    public void getNewOwnerChallenge(String room) {

        Battle battle = findBattleByToken(room);

        if (isBatalhaFinished(battle)) {
            throw new ForbiddenException("Batalha já finalizada.");
        }

        String topic = constructTopic(room, "/owner/challenge");

        ChallengeResponse randomChallenge = challengeService.getRandomChallenge();
        ChallengeBattleResponse challengeBattleResponse = modelMapper.map(randomChallenge, ChallengeBattleResponse.class);

        template.convertAndSend(topic, challengeBattleResponse);

        updateOwnerPoints(room);
    }

    public void getNewOpponentChallenge(String room) {

        Battle battle = findBattleByToken(room);

        if (isBatalhaFinished(battle)) {
            throw new ForbiddenException("Batalha já finalizada.");
        }

        String topic = constructTopic(room, "/opponent/challenge");

        ChallengeResponse randomChallenge = challengeService.getRandomChallenge();
        ChallengeBattleResponse challengeBattleResponse = modelMapper.map(randomChallenge, ChallengeBattleResponse.class);

        template.convertAndSend(topic, challengeBattleResponse);

        updateOpponentPoints(room);
    }

    public void updateOwnerPoints(String room) {

        Battle battle = findBattleByToken(room);
        Integer points;

        if (battle.getOwnerPoints() == null) {
            points = 0;
        } else {
            points = battle.getOwnerPoints() + 1;
        }

        battle.setOwnerPoints(points);

        repository.save(battle);
    }

    public void updateOpponentPoints(String room) {

        Battle battle = findBattleByToken(room);
        Integer points;

        if (battle.getOpponentPoints() == null) {
            points = 0;
        } else {
            points = battle.getOpponentPoints() + 1;
        }

        battle.setOpponentPoints(points);

        repository.save(battle);
    }

    public void getWinner(String room) {

        String topic = constructTopic(room, "/winner");
        Battle battle = findBattleByToken(room);

        if (battle.getComputed()) {
            throw new ForbiddenException("Pontos de batalha ja computados");
        }
        if (!isBatalhaFinished(battle)) {
            throw new ForbiddenException("Batalha ainda não acabou");
        }
        battle.setComputed(true);
        repository.save(battle);

        computateRanking(battle, topic);
    }

    public void computateRanking(Battle battle, String topic) {

        Integer ownerPoints = battle.getOwnerPoints();
        Integer opponentPoints = battle.getOpponentPoints();
        Integer ownerRanking = 0;
        Integer opponentRanking = 0;

        if (ownerPoints > opponentPoints) {

            Integer ownerDelta = calculateOwnerDelta(battle);
            ownerRanking = 25 + ownerDelta;
            opponentRanking = -15 - ownerDelta;
        }
        if (opponentPoints > ownerPoints) {

            Integer opponentDelta = calculateOpponentDelta(battle);
            opponentRanking = 25 + opponentDelta;
            ownerRanking = -15 - opponentDelta;
        }

        userService.updateUserRanking(battle.getOwner(), ownerRanking);
        userService.updateUserRanking(battle.getOpponent(), opponentRanking);
        BattleRankingResponse battleRankingResponse = BattleMapper.toBattleRankingResponse(ownerRanking, opponentRanking);

        template.convertAndSend(topic, battleRankingResponse);
    }

    public Boolean isBatalhaFinished(Battle battle) {

        Date currentDate = new Date();
        Long time = currentDate.getTime();

        return (battle.getEndTime() + 500) < time;
    }

    public Battle findBattleByToken(String token) {

        try {
            return ofNullable(repository.findByToken(Integer.valueOf(token)))
                    .orElseThrow(() -> new NotFoundException("Batalha não cadastrada."));
        } catch (Exception exception) {
            throw new BadRequestException("Token Inválido");
        }
    }

    public String constructTopic(String room, String endUrl) {

        return "/room/" + room + endUrl;
    }

    public Integer calculateOwnerDelta(Battle battle) {

        Integer differenceRanking = battle.getOpponent().getRanking() - battle.getOwner().getRanking();
        Integer differencePoints = battle.getOwnerPoints() - battle.getOpponentPoints();
        Integer delta = (int) Math.floor(differenceRanking / 40.0);

        if (differenceRanking > 0) {
            delta = (int) Math.floor(delta * (differencePoints * 0.1 + 1));
        } else {
            delta = (int) Math.ceil(delta / (differencePoints * 0.1 + 1));
        }

        return Math.min(Math.max(delta, -10), 10);
    }

    public Integer calculateOpponentDelta(Battle battle) {

        Integer differenceRanking = battle.getOwner().getRanking() - battle.getOpponent().getRanking();
        Integer differencePoints = battle.getOpponentPoints() - battle.getOwnerPoints();
        Integer delta = (int) Math.floor(differenceRanking / 40.0);

        if (differenceRanking > 0) {
            delta = (int) Math.floor(delta * (differencePoints * 0.1 + 1));
        } else {
            delta = (int) Math.ceil(delta / (differencePoints * 0.1 + 1));
        }

        return Math.min(Math.max(delta, -10), 10);
    }
}
