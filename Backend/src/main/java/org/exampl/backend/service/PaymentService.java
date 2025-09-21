package org.exampl.backend.service;

import org.exampl.backend.dto.PaymentDTO;
import org.exampl.backend.dto.UserDataDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {
    public String getNextPaymentId();
    UserDataDTO processPayment(PaymentDTO paymentDTO);
}
