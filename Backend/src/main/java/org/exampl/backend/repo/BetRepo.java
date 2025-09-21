package org.exampl.backend.repo;

import org.exampl.backend.entity.Bet;
import org.exampl.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BetRepo extends JpaRepository<Bet,String> {
    Bet findTopByOrderByBetIdDesc();
}
