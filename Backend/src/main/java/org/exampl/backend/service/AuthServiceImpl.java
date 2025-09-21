package org.exampl.backend.service;

import lombok.RequiredArgsConstructor;
import org.exampl.backend.dto.AuthDTO;
import org.exampl.backend.dto.AuthResponseDTO;
import org.exampl.backend.dto.RegisterDTO;
import org.exampl.backend.entity.Role;
import org.exampl.backend.entity.User;
import org.exampl.backend.repo.UserDataRepository;
import org.exampl.backend.repo.UserRepository;
import org.exampl.backend.utill.JwtUtill;
import org.exampl.backend.utill.SendMailUtill;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtill jwtUtil;
    private final SendMailUtill sendMailUtill;

    public AuthResponseDTO authenticate(AuthDTO authDTO) {
        User user=
                userRepository.findByUsername(authDTO.getUsername())
                        .orElseThrow(
                                ()->new UsernameNotFoundException
                                        ("Username not found"));
        if (!passwordEncoder.matches(
                authDTO.getPassword(),
                user.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }
        String accessToken=jwtUtil.generateAccessToken(authDTO.getUsername());
        String refreshToken=jwtUtil.generateRefreshToken(authDTO.getUsername());

        if(!accessToken.isEmpty() || !refreshToken.isEmpty()){
            sendMailUtill.sendLoginAlertEmail("praveenrusiru031@gmail.com", authDTO.getUsername(),"","","");
        }
        return  new AuthResponseDTO(accessToken, refreshToken,authDTO.getUsername(),user.getBalance(),user.getId(),user.getEmail());
    }
    public boolean register(RegisterDTO registerDTO) {
        boolean isUsernameExist=false;
        boolean isEmailExist=false;
        boolean isNicExist=false;
        boolean isPasswordMatches=false;
        // Username: 3–16 characters, letters, numbers, underscores
        String usernameRegex = "^[a-zA-Z0-9_]{3,16}$";
        // Password: At least 8 chars, one uppercase, one lowercase, one digit, one special
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        // Email validation
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        // NIC (Sri Lankan): 12 digits OR 9 digits + V/v/X/x
        String nicRegex = "^(?:\\d{12}|\\d{9}[VvXx])$";


        if(Pattern.matches(usernameRegex, registerDTO.getUsername())) {
            isUsernameExist=false;
        }
        if(Pattern.matches(emailRegex, registerDTO.getEmail())) {
            isEmailExist=false;
        }
        if(Pattern.matches(nicRegex, registerDTO.getNic())) {
            isNicExist=false;
        }
        if(Pattern.matches(passwordRegex, registerDTO.getPassword())) {
            isPasswordMatches=false;
        }
        if(userRepository.findByUsername(
                registerDTO.getUsername()).isPresent()){

            isUsernameExist=true;
            throw new RuntimeException("Username already exists");
        }
        if(userRepository.findByEmail(registerDTO.getEmail()).isPresent()){
            isEmailExist=true;
            throw new RuntimeException("Email already exists");
        }
        if(userRepository.findByNic(registerDTO.getNic()).isPresent()){
            isNicExist=true;
            throw new RuntimeException("Nic already exists");
        }

        ////

        User lastUser = userRepository.findTopByOrderByIdDesc();

        String newId;
        if (lastUser == null) {
            // No users yet
            newId = "U00-001";
        } else {
            String lastId = lastUser.getId(); // e.g., U00-999

            // Split into prefix and number
            String[] parts = lastId.split("-");
            String prefix = parts[0];  // U00
            int number = Integer.parseInt(parts[1]); // 999

            // Get prefix numeric part (U00 → 0)
            int prefixNum = Integer.parseInt(prefix.substring(1));

            // Increment logic
            if (number == 999) {
                prefixNum++;
                number = 0; // reset
            } else {
                number++;
            }

            // Build new ID
            newId = String.format("U%02d-%03d", prefixNum, number);
        }

        ////
        System.out.println("check is exist "+newId+" "+isUsernameExist+isEmailExist+isNicExist+isPasswordMatches);
        if(!(isUsernameExist || isEmailExist || isNicExist)){
            User user=User.builder()
                    .id(newId)
                    .username(registerDTO.getUsername())
                    .password(passwordEncoder.encode(
                            registerDTO.getPassword()))
                    .role(Role.valueOf(registerDTO.getRole()))
                    .email(registerDTO.getEmail())
                    .nic(registerDTO.getNic())
                    .build();
            User save = userRepository.save(user);
            if(save!=null){
                sendMailUtill.sendWelcomeEmail(registerDTO.getEmail(),registerDTO.getUsername());
            }
            return  save!=null?true:false;
        }
            return false;

    }
    public boolean userNameValidate(String username) {
        boolean present = userRepository.findByUsername(username).isPresent();
        System.out.println("isPresent "+present);
        return present;
    }

    public boolean emailValidate(String email) {
        boolean present = userRepository.findByEmail(email).isPresent();
        System.out.println("isPresent "+present);
        return present;
    }

    public boolean nicValidate(String nic) {
        boolean present = userRepository.findByNic(nic).isPresent();
        System.out.println("Nic isPresent "+present);
        return present;
    }

}
