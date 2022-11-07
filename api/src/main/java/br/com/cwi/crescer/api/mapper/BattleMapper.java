package br.com.cwi.crescer.api.mapper;

import br.com.cwi.crescer.api.domain.Battle;
import br.com.cwi.crescer.api.domain.User;
import br.com.cwi.crescer.api.representation.response.BattleRankingResponse;
import br.com.cwi.crescer.api.representation.response.ChallengeBattleResponse;
import br.com.cwi.crescer.api.representation.response.ChallengeResponse;
import br.com.cwi.crescer.api.representation.response.PayloadBattleResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

public class BattleMapper {

    private BattleMapper() {
    }

    public static Battle toDomain(Integer token, User user) {
        return new Battle(token, user);
    }

    public static BattleRankingResponse toBattleRankingResponse(Integer ownerRanking, Integer opponentRanking) {

        return new BattleRankingResponse(ownerRanking, opponentRanking);
    }

    public static PayloadBattleResponse toPayloadBattleResponse(Page<ChallengeResponse> randomChallengePage) {

        ModelMapper modelMapper = new ModelMapper();

        ChallengeResponse challengeResponseOwner = randomChallengePage.getContent().get(0);
        ChallengeResponse challengeResponseOpponent = randomChallengePage.getContent().get(1);

        ChallengeBattleResponse challengeBattleResponseOwner = modelMapper.map(challengeResponseOwner, ChallengeBattleResponse.class);
        ChallengeBattleResponse challengeBattleResponseOpponent = modelMapper.map(challengeResponseOpponent, ChallengeBattleResponse.class);

        return new PayloadBattleResponse(
                challengeBattleResponseOwner,
                challengeBattleResponseOpponent
        );
    }
}
