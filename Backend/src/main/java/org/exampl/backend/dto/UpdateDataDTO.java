package org.exampl.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.exampl.backend.entity.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateDataDTO {
    private String id;
    private String oldUsername;
    private String username;
    private String newPassword;
    private String confirmnewPassword;
    private String nic;
    private String email;
    private Role role;
}
