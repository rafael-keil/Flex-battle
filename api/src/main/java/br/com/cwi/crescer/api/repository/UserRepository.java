package br.com.cwi.crescer.api.repository;

import br.com.cwi.crescer.api.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    @Query("SELECT u FROM User u ORDER BY u.ranking DESC")
    Page<User> getLeaderboard(Pageable pageable);
}
