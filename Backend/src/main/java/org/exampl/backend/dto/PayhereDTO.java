package org.exampl.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayhereDTO {
    private String paymentId;
    private double amount;
    private String hash;
    private String username;
    private String email;
}
