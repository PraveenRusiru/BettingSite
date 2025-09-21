package org.exampl.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.exampl.backend.dto.PaymentDTO;
import org.exampl.backend.dto.UserDataDTO;
import org.exampl.backend.entity.Payment;
import org.exampl.backend.entity.User;
import org.exampl.backend.repo.UserRepository;
import org.exampl.backend.utill.SendMailUtill;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
     private final PaymentRepo paymentRepo;
     private final UserRepository userRepository;
     private final SendMailUtill sendMailUtill;
    @Override
    public String getNextPaymentId() {
        Payment payment = paymentRepo.findFirstByOrderByPaymentIdDesc();

        String newId;
        if (payment == null) {
            // No users yet
            newId = "P00-001";
        } else {
            String lastId = payment.getPaymentId(); // e.g., U00-999

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
            newId = String.format("P%02d-%03d", prefixNum, number);
        }
        System.out.println("New Payment Id: " + newId);
        return newId;
    }

    @Transactional
    @Override
    public UserDataDTO processPayment(PaymentDTO paymentDTO) {
       User user = userRepository.findByUsername(paymentDTO.getUsername()).get();

        Payment payment=Payment.builder().
                paymentId(paymentDTO.getPaymentId()).
                amount(paymentDTO.getAmount()).
                date(paymentDTO.getDate()).
                paymentType(paymentDTO.getPaymentType()).
                status("completed").
                user(user).
                build();

        paymentRepo.save(payment);


        user.setBalance(user.getBalance()+paymentDTO.getAmount());
        User user1 = userRepository.save(user);
        sendMailUtill.sendDepositConfirmationEmail(user1.getEmail(),user1.getUsername(),String.valueOf(paymentDTO.getAmount()),"");
        UserDataDTO userDataDTO = UserDataDTO.builder().
                balance(user1.getBalance())
        .build();
        return userDataDTO;
    }
}
