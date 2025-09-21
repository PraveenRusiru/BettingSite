package org.exampl.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.exampl.backend.dto.BetDTO;
import org.exampl.backend.entity.Bet;
import org.exampl.backend.entity.BetStatus;
import org.exampl.backend.entity.User;
import org.exampl.backend.repo.BetRepo;
import org.exampl.backend.repo.UserDataRepository;
import org.exampl.backend.repo.UserRepository;
import org.exampl.backend.utill.SendMailUtill;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;

@Service
@RequiredArgsConstructor
public class BetServiceImpl implements BetService {
    private final BetRepo betRepo;
    private final UserDataRepository userDataRepository;
    private final SendMailUtill sendMailUtill;
    private final UserRepository userRepository;
   @Transactional
    @Override
    public boolean placeBet(BetDTO betDTO) {

        Bet lastBet = betRepo.findTopByOrderByBetIdDesc();

        String newId;
        if (lastBet == null) {
            // No users yet
            newId = "B00-001";
        } else {
            String lastId = lastBet.getBetId(); // e.g., U00-999

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
            newId = String.format("B%02d-%03d", prefixNum, number);
        }

        Bet newBet=Bet.builder().
                betId(newId).
                amount(betDTO.getAmount()).
                date(new Date(System.currentTimeMillis())).
                potentialReturn(betDTO.getPotentialReturn()).
                stake(betDTO.getStake()).
                time(new Time(System.currentTimeMillis())).
                status(BetStatus.PENDING).
                user(userDataRepository.findByUsername(betDTO.getUsername())).
                build();
        betRepo.save(newBet);

        User user = userDataRepository.findByUsername(betDTO.getUsername());
        user.setBalance(user.getBalance() - betDTO.getAmount());
        userRepository.save(user);
        sendMailUtill.sendBetConfirmationEmail(user.getEmail(),user.getUsername(),"","",String.valueOf(betDTO.getAmount()),String.valueOf(betDTO.getPotentialReturn()),newId);
        return true;
    }
}
