package br.com.cwi.crescer.api.repository;

import br.com.cwi.crescer.api.domain.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Integer> {

    @Query("SELECT c FROM Challenge c " +
            "ORDER BY RAND()")
    Page<Challenge> getRandomChallenge(Pageable pageable);
}
