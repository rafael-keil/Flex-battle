package br.com.cwi.crescer.api.mapper;

import br.com.cwi.crescer.api.domain.Answer;
import br.com.cwi.crescer.api.domain.Challenge;
import br.com.cwi.crescer.api.representation.response.ChallengeResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChallengeMapper {

    private ChallengeMapper() {
    }

    public static ChallengeResponse toResponse(Challenge challenge) {

        List<List<String>> proprieties = new ArrayList<>();

        for (Answer answer : challenge.getAnswerList()) {
            List<String> property = Arrays.asList(answer.getProperty(), answer.getValue());
            proprieties.add(property);
        }

        return new ChallengeResponse(proprieties, challenge.getObjects(), challenge.getHint());
    }
}
