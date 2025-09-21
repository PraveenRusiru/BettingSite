package org.exampl.backend.service;

import org.exampl.backend.dto.AuthDTO;
import org.exampl.backend.dto.UpdateDataDTO;
import org.exampl.backend.dto.UserDataDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserDataService {
    public UserDataDTO getAllUserData(String username);
    public boolean validateCurrentPassword(AuthDTO authDTO);
    public boolean updateUserData(UpdateDataDTO updateDataDTO);
    public String findUserIdByUsername(String username);
}
