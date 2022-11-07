package br.com.cwi.crescer.api.fixture;

import br.com.cwi.crescer.api.domain.Answer;

public class AnswerFixture {

    public static Answer answer() {

        return new Answer(
                "alignItems",
                "center"
        );
    }

    public static Answer answerSecondary() {

        return new Answer(
                "justifyContent",
                "center"
        );
    }
}
