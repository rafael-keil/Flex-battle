package br.com.cwi.crescer.api.fixture;

import br.com.cwi.crescer.api.domain.Answer;
import br.com.cwi.crescer.api.domain.Challenge;
import br.com.cwi.crescer.api.representation.response.ChallengeBattleResponse;
import br.com.cwi.crescer.api.representation.response.ChallengeResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChallengeFixture {

    public static Challenge challenge() {

        List<Answer> answerList = new ArrayList<>();
        answerList.add(AnswerFixture.answer());
        answerList.add(AnswerFixture.answerSecondary());

        return new Challenge(
                answerList,
                2,
                "hint"
        );
    }

    public static Challenge challengeAlternative() {

        List<Answer> answerList = new ArrayList<>();
        answerList.add(AnswerFixture.answer());
        answerList.add(AnswerFixture.answerSecondary());

        return new Challenge(
                answerList,
                3,
                "hint2"
        );
    }

    public static ChallengeResponse challengeResponse() {

        List<List<String>> answerList = new ArrayList<>();
        answerList.add(new ArrayList<>(Arrays.asList("alignItems", "center")));
        answerList.add(new ArrayList<>(Arrays.asList("justifyContent", "center")));

        return new ChallengeResponse(
                answerList,
                2,
                "hint"
        );
    }

    public static ChallengeResponse challengeResponseAlternative() {

        List<List<String>> answerList = new ArrayList<>();
        answerList.add(new ArrayList<>(Arrays.asList("alignItems", "flex-end")));
        answerList.add(new ArrayList<>(Arrays.asList("justifyContent", "center")));

        return new ChallengeResponse(
                answerList,
                3,
                "hint2"
        );
    }

    public static Page<Challenge> challengePageWithOne() {

        Challenge challenge = challenge();
        List<Challenge> challengeList = new ArrayList<>(Collections.singletonList(challenge));
        return new PageImpl<>(challengeList);
    }

    public static Page<Challenge> challengePageWithTwo() {

        Challenge challenge = challenge();
        Challenge challengeAlternative = challengeAlternative();
        List<Challenge> challengeResponseList = Arrays.asList(challenge, challengeAlternative);
        return new PageImpl<>(challengeResponseList);
    }

    public static Page<ChallengeResponse> challengeResponsePageWithTwo() {

        ChallengeResponse challengeResponse = challengeResponse();
        ChallengeResponse challengeResponseAlternative = challengeResponseAlternative();
        List<ChallengeResponse> challengeResponseList = Arrays.asList(challengeResponse, challengeResponseAlternative);
        return new PageImpl<>(challengeResponseList);
    }

    public static ChallengeBattleResponse challengeBattleResponse() {

        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(challengeResponse(), ChallengeBattleResponse.class);
    }
}
