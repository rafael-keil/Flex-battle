package br.com.cwi.crescer.api.fixture;

import br.com.cwi.crescer.api.domain.Battle;
import br.com.cwi.crescer.api.representation.request.InputRequest;
import br.com.cwi.crescer.api.representation.response.BattleRankingResponse;
import br.com.cwi.crescer.api.representation.response.ChallengeBattleResponse;
import br.com.cwi.crescer.api.representation.response.ChallengeResponse;
import br.com.cwi.crescer.api.representation.response.PayloadBattleResponse;
import org.modelmapper.ModelMapper;

public class BattleFixture {

    public static Battle battle() {

        return new Battle(123456, UserFixture.user());
    }

    public static Battle battleWithPoints() {

        Battle battle = battle();
        battle.setOpponent(UserFixture.userAlternative());
        battle.setOwnerPoints(1);
        battle.setOpponentPoints(1);

        return battle;
    }

    public static Battle BattleWithPointsReverse() {

        Battle battle = battleWithPoints();
        battle.setOwner(UserFixture.userAlternative());
        battle.setOpponent(UserFixture.user());

        return battle;
    }

    public static PayloadBattleResponse payloadBattleResponse() {

        ModelMapper modelMapper = new ModelMapper();

        ChallengeResponse challengeResponse = ChallengeFixture.challengeResponse();
        ChallengeResponse challengeResponseAlternative = ChallengeFixture.challengeResponseAlternative();

        return new PayloadBattleResponse(
                modelMapper.map(challengeResponse, ChallengeBattleResponse.class),
                modelMapper.map(challengeResponseAlternative, ChallengeBattleResponse.class)
        );
    }

    public static InputRequest completeInputRequest() {

        return new InputRequest("input");
    }

    public static BattleRankingResponse battleRankingResponse() {

        return new BattleRankingResponse(30, -20);
    }

    public static BattleRankingResponse battleRankingResponseAlternative() {

        return new BattleRankingResponse(-20, 30);
    }
}
