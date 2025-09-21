package org.exampl.backend.service;

import org.exampl.backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends JpaRepository<Payment,String> {
    Payment findFirstByOrderByPaymentIdDesc();
}
