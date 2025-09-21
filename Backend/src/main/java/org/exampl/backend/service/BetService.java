package org.exampl.backend.service;

import org.exampl.backend.dto.BetDTO;
import org.springframework.stereotype.Service;

@Service
public interface BetService {
    public boolean placeBet(BetDTO betDTO);
}
