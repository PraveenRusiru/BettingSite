package org.exampl.backend.service;

import org.exampl.backend.dto.AuthDTO;
import org.exampl.backend.dto.AuthResponseDTO;
import org.exampl.backend.dto.RegisterDTO;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    public AuthResponseDTO authenticate(AuthDTO authDTO);
    public boolean register(RegisterDTO registerDTO);
    public boolean userNameValidate(String username);
    public boolean emailValidate(String email);
    public boolean nicValidate(String nic);
}
