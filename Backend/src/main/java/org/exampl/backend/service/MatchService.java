package org.exampl.backend.service;

import org.exampl.backend.dto.MatchDataDTO;
import org.exampl.backend.entity.CricketMatch;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MatchService {
    public CricketMatch existMatch(MatchDataDTO matchDataDTO) ;
    public boolean saveMatch(MatchDataDTO matchDataDTO);
}
