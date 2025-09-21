package org.exampl.backend.service;

import lombok.RequiredArgsConstructor;
import org.exampl.backend.dto.MatchDataDTO;
import org.exampl.backend.entity.CricketMatch;
import org.exampl.backend.entity.User;
import org.exampl.backend.repo.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {
    private final MatchRepository matchRepository;
    @Override
    public CricketMatch existMatch(MatchDataDTO matchDataDTO) {
//                for (MatchDataDTO matchDataDTO : matchDataDTOS) {

                    CricketMatch cricketMatch = matchRepository.findTopByOrderByMatchIdDesc();

                    String newId;
                    if (cricketMatch==null) {
                        // No users yet
                        newId = "M00-001";
                    } else {
                        String lastId = cricketMatch.getMatchId(); // e.g., U00-999

                        // Split into prefix and number
                        String[] parts = lastId.split("-");
                        String prefix = parts[0];  // U00
                        int number = Integer.parseInt(parts[1]); // 999

                        // Get prefix numeric part (U00 → 0)
                        int prefixNum = Integer.parseInt(prefix.substring(1));

                        // Increment logic
                        if (number == 999) {
                            prefixNum++;
                            number = 0; // reset
                        } else {
                            number++;
                        }

                        // Build new ID
                        newId = String.format("M%02d-%03d", prefixNum, number);
                    }
                    matchDataDTO.setId(newId);
        Optional<CricketMatch> availableMatch = matchRepository.findFirstByTeam1AndTeam2AndIsLiveAndLocationAndMatchFormatAndMatchSeries(matchDataDTO.getBattingTeam(), matchDataDTO.getBowlingTeam(), true, matchDataDTO.getVenue(), matchDataDTO.getMatchFormat(), matchDataDTO.getTournament());
        System.out.println("isExist:"+availableMatch.isPresent());
        if(availableMatch.isPresent()){
            return availableMatch.get();
        }
        return null;
    }
    @Override
    public boolean saveMatch(MatchDataDTO matchDataDTO) {
        CricketMatch matches=CricketMatch.builder().
                matchId(matchDataDTO.getId()).
                location(matchDataDTO.getVenue()).
                matchFormat(matchDataDTO.getMatchFormat()).
                matchSeries(matchDataDTO.getTournament()).
                team1(matchDataDTO.getBattingTeam()).
                team2(matchDataDTO.getBowlingTeam()).
                isLive(true)
                .build();
        CricketMatch save = matchRepository.save(matches);
        if(save!=null) {
            return true;
        }
        return false;
    }
}
