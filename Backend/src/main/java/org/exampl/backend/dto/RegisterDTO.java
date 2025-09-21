package org.exampl.backend.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
public class RegisterDTO {
    private String username;
    private String password;
    private  String role;
    private String nic;
    private String email;
}
