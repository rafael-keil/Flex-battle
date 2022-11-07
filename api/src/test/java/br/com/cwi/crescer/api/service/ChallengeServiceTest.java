package br.com.cwi.crescer.api.service;

import br.com.cwi.crescer.api.domain.Challenge;
import br.com.cwi.crescer.api.fixture.ChallengeFixture;
import br.com.cwi.crescer.api.repository.ChallengeRepository;
import br.com.cwi.crescer.api.representation.response.ChallengeResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChallengeServiceTest {

    @InjectMocks
    private ChallengeService tested;

    @Mock
    private ChallengeRepository repository;

    @Test
    public void quandoChamarGetRandomChelengeDeveRetornarChelenge() {

        Pageable pageable = PageRequest.of(0, 1);
        Page<Challenge> challengePage = ChallengeFixture.challengePageWithOne();
        ChallengeResponse challengeResponse = ChallengeFixture.challengeResponse();

        when(repository.getRandomChallenge(pageable))
                .thenReturn(challengePage);

        ChallengeResponse randomChallenge = tested.getRandomChallenge();

        verify(repository).getRandomChallenge(pageable);
        assertEquals(challengeResponse, randomChallenge);
    }

    @Test
    public void quandoChamarGetRandomChallengePageDeveRetornarChallengeResponsePage() {

        Pageable pageable = PageRequest.of(0, 2);
        Page<Challenge> challengePage = ChallengeFixture.challengePageWithTwo();
        Page<ChallengeResponse> challengeResponsePage = ChallengeFixture.challengeResponsePageWithTwo();

        when(repository.getRandomChallenge(pageable))
                .thenReturn(challengePage);

        Page<ChallengeResponse> challengeResponsePageReturned = tested.getRandomChallengePage();

        verify(repository).getRandomChallenge(pageable);
        assertTrue(
                IntStream.range(0, challengeResponsePage.getSize())
                        .allMatch(index -> challengeResponsePage.getContent().get(index).getHint()
                                .equals(challengeResponsePageReturned.getContent().get(index).getHint())
                        )
        );
    }
}