package br.com.cwi.crescer.api.mapper;

import br.com.cwi.crescer.api.domain.Challenge;
import br.com.cwi.crescer.api.fixture.ChallengeFixture;
import br.com.cwi.crescer.api.representation.response.ChallengeResponse;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChallengeMapperTest {

    @Test
    public void quandoInformarChelengeDeveRetornarUmChelengeResponse() {

        Challenge challenge = ChallengeFixture.challenge();

        ChallengeResponse challengeResponse = ChallengeMapper.toResponse(challenge);

        assertEquals(challenge.getObjects(), challengeResponse.getObjects());
        assertEquals(challenge.getHint(), challengeResponse.getHint());
        assertTrue(
                IntStream.range(0, challenge.getAnswerList().size())
                        .allMatch(index ->
                                challenge.getAnswerList().get(index).getProperty().equals(challengeResponse.getAnswer().get(index).get(0)) &&
                                        challenge.getAnswerList().get(index).getValue().equals(challengeResponse.getAnswer().get(index).get(1))
                        )
        );
    }
}