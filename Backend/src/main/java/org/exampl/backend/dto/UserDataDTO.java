package org.exampl.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDataDTO {
    private String id;
    private String username;
    private String email;
    private String password;
    private String nic;
    private double balance;

}
