package br.com.cwi.crescer.api.mapper;

import br.com.cwi.crescer.api.domain.Battle;
import br.com.cwi.crescer.api.domain.User;
import br.com.cwi.crescer.api.fixture.BattleFixture;
import br.com.cwi.crescer.api.fixture.ChallengeFixture;
import br.com.cwi.crescer.api.fixture.UserFixture;
import br.com.cwi.crescer.api.representation.response.BattleRankingResponse;
import br.com.cwi.crescer.api.representation.response.ChallengeResponse;
import br.com.cwi.crescer.api.representation.response.PayloadBattleResponse;
import org.junit.Test;
import org.springframework.data.domain.Page;

import static org.junit.Assert.assertEquals;

public class BattleMapperTest {

    @Test
    public void quandoInformarUserETokenDeveRetornarUmBattle() {

        User user = UserFixture.user();
        Integer token = 123456;

        Battle battle = BattleMapper.toDomain(token, user);

        assertEquals(user, battle.getOwner());
        assertEquals(token, battle.getToken());
    }

    @Test
    public void quandoInformarDoisRankingDeveRetornarBattleRankingResponse() {

        Integer ownerRanking = 30;
        Integer opponentRanking = -20;
        BattleRankingResponse battleRankingResponse = BattleFixture.battleRankingResponse();

        BattleRankingResponse battleRankingResponseReturned = BattleMapper.toBattleRankingResponse(ownerRanking, opponentRanking);

        assertEquals(battleRankingResponse.getOwnerRanking(), battleRankingResponseReturned.getOwnerRanking());
        assertEquals(battleRankingResponse.getOpponentRanking(), battleRankingResponseReturned.getOpponentRanking());
    }

    @Test
    public void quandoInformarChallengeResponsePageDeveRetornarPayloadBattleResponse() {

        PayloadBattleResponse payloadBattleResponse = BattleFixture.payloadBattleResponse();
        Page<ChallengeResponse> challengeResponsePage = ChallengeFixture.challengeResponsePageWithTwo();

        PayloadBattleResponse payloadBattleResponseReturned = BattleMapper.toPayloadBattleResponse(challengeResponsePage);

        assertEquals(payloadBattleResponse.getChallengeOwner(), payloadBattleResponseReturned.getChallengeOwner());
        assertEquals(payloadBattleResponse.getChallengeOpponent(), payloadBattleResponseReturned.getChallengeOpponent());
    }
}