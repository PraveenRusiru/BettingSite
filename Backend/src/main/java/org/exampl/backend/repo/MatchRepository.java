package org.exampl.backend.repo;

import org.exampl.backend.entity.CricketMatch;
import org.exampl.backend.entity.MatchFormat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<CricketMatch,String> {
    CricketMatch findTopByOrderByMatchIdDesc();

    Optional<CricketMatch> findFirstByTeam1AndTeam2AndIsLiveAndLocationAndMatchFormatAndMatchSeries(String team1,String team2,boolean isLive,String location,MatchFormat matchFormat,String matchSeries);
//    Optional<CricketMatch> findFirstByTeam1AndTeam2AndIsLiveAndLocationAndMatchFormatAndMatchSeries(String team1, String team2, boolean isLive, String location, MatchFormat matchFormat, String matchSeries);


}
