package br.com.cwi.crescer.api.repository;

import br.com.cwi.crescer.api.domain.Battle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BattleRepository extends JpaRepository<Battle, Integer> {

    Battle findByToken(Integer token);
}
