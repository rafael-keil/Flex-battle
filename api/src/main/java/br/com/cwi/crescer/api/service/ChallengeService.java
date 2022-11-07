package br.com.cwi.crescer.api.service;

import br.com.cwi.crescer.api.domain.Challenge;
import br.com.cwi.crescer.api.mapper.ChallengeMapper;
import br.com.cwi.crescer.api.repository.ChallengeRepository;
import br.com.cwi.crescer.api.representation.response.ChallengeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ChallengeService {

    @Autowired
    private ChallengeRepository repository;

    @Autowired
    private SecurityApiService securityApiService;

    public ChallengeResponse getRandomChallenge() {

        Pageable pageable = PageRequest.of(0, 1);

        Challenge challenge = repository.getRandomChallenge(pageable).getContent().get(0);

        return ChallengeMapper.toResponse(challenge);
    }

    public Page<ChallengeResponse> getRandomChallengePage() {

        Pageable pageable = PageRequest.of(0, 2);

        return repository.getRandomChallenge(pageable).map(ChallengeMapper::toResponse);
    }
}
