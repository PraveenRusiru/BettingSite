package org.exampl.backend.dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.exampl.backend.entity.PaymentType;
import org.exampl.backend.entity.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private String username;
    private PaymentType paymentType;
    private String date;
    private double amount;
    private String status;
    private String paymentId;
}
