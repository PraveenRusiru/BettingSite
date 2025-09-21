package org.exampl.backend.service;

import lombok.RequiredArgsConstructor;
import org.exampl.backend.dto.AuthDTO;
import org.exampl.backend.dto.UpdateDataDTO;
import org.exampl.backend.dto.UserDataDTO;
import org.exampl.backend.entity.User;
import org.exampl.backend.repo.UserDataRepository;
import org.exampl.backend.repo.UserRepository;
import org.exampl.backend.utill.JwtUtill;
import org.exampl.backend.utill.SendMailUtill;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDataServiceImpl implements UserDataService {
    private final UserDataRepository userDataRepository;
    private final UserRepository userRepository;
        private final JwtUtill jwtUtil;
    private final SendMailUtill sendMailUtill;
    private final PasswordEncoder passwordEncoder;

    public UserDataDTO getAllUserData(String username) {
        User user = userDataRepository.findByUsername(username);
        UserDataDTO userDataDTO = UserDataDTO.builder().
                                    username(user.getUsername()).
                                    id(user.getId()).
                                    email(user.getEmail()).
                                    nic(user.getNic()).
                                    password(user.getPassword())
                                    .build();
        return userDataDTO;
    }

    public boolean validateCurrentPassword(AuthDTO authDTO) {
        User user = userRepository.findByUsername(authDTO.getUsername())
                .orElseThrow(
                        () -> new UsernameNotFoundException
                                ("Username not found"));

        if (!passwordEncoder.matches(
                authDTO.getPassword(),
                user.getPassword())) {

//            throw new BadCredentialsException("Incorrect password");
            return false;
        }
        return true;
    }
    public boolean updateUserData(UpdateDataDTO updateDataDTO) {
        return userDataRepository.findById(updateDataDTO.getId())
                .map(existingUser -> {
                    if (updateDataDTO.getUsername() != null) {
                        existingUser.setUsername(updateDataDTO.getUsername());
                    }
                    if (updateDataDTO.getEmail() != null) {
                        existingUser.setEmail(updateDataDTO.getEmail());
                    }
                    if (updateDataDTO.getNic() != null) {
                        existingUser.setNic(updateDataDTO.getNic());
                    }
                    if (updateDataDTO.getNewPassword() != null) {
                        existingUser.setPassword(passwordEncoder.encode(updateDataDTO.getNewPassword()));
                    }
                    if (updateDataDTO.getRole() != null) {
                        existingUser.setRole(updateDataDTO.getRole());
                    }

                    userDataRepository.save(existingUser);
                    return true;
                })
                .orElse(false);
    }
    public String findUserIdByUsername(String username) {
        User user = userDataRepository.findByUsername(username);
        if (user != null) {
            return user.getId();
        }
        return null;
    }
}
