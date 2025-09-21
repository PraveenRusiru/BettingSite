package org.exampl.backend.controller;

import lombok.RequiredArgsConstructor;
import org.exampl.backend.dto.ApiResponse;
import org.exampl.backend.dto.AuthDTO;
import org.exampl.backend.dto.AuthResponseDTO;
import org.exampl.backend.dto.RegisterDTO;
import org.exampl.backend.service.AuthServiceImpl;
import org.exampl.backend.utill.JwtUtill;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor

public class AuthController {
    private final AuthServiceImpl authServiceImpl;
    private final JwtUtill jwt;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody RegisterDTO registerDTO) {
//        authService.authenticate()
        return ResponseEntity.ok(new ApiResponse(200,"OK", authServiceImpl.register(registerDTO)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody AuthDTO authDTO) {
        AuthResponseDTO authResponseDTO = authServiceImpl.authenticate(authDTO);
        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", authResponseDTO.getRefreshToken())
                .httpOnly(true)
                .secure(false)          // must be false for local HTTP
                .path("/")              // make it accessible to all endpoints
                .maxAge(60L * 60 * 24 * 30)
                .sameSite("Lax")        // "None" requires secure HTTPS
                .build();
        System.out.println("login refreshCookie "+refreshCookie.getValue());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(new ApiResponse(200,"OK",authResponseDTO));

    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refresh(@CookieValue(name = "refresh_token", required = false) String refreshToken) {
        if (refreshToken == null || !jwt.validateToken(refreshToken)) {
            System.out.println("refresh tocken null or refreshtocken is invalid "+refreshToken+" "+jwt.validateToken(refreshToken));
            return ResponseEntity.status(401).body(new ApiResponse(401,"error", "Invalid or missing refresh token"));
        }
// 🔑 Verify token type
        if (!"refresh".equals(jwt.getTokenType(refreshToken))) {
            System.out.println("tocken type is invalid "+jwt.getTokenType(refreshToken));
            return ResponseEntity.status(401).body(new ApiResponse(401, "error", "Token is not a refresh token"));
        }

        // 🔑 Get username from refresh token
        String subject = jwt.extractUsername(refreshToken);
        System.out.println("refresh tocken "+refreshToken);
        // 🔑 Generate new access token
        String newAccess = jwt.generateAccessToken(subject);
        System.out.println("subject is "+subject+"refresh tocken is "+refreshToken+" newAccess is "+newAccess);
        return ResponseEntity.ok(new ApiResponse(200, "OK", Map.of(
                "accessToken", newAccess,
                "tokenType", "Bearer",
                "expiresIn", 60 * 15 // e.g. 15 min
        )));
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse> getAll() {
        return ResponseEntity.ok(new ApiResponse(200,"OK","dddddd"));
    }
    @GetMapping("/validateUsername")
    public ResponseEntity<ApiResponse> validateUserName(@RequestParam("username") String username) {
        return ResponseEntity.ok(new ApiResponse(200,"OK", authServiceImpl.userNameValidate(username)));
    }
    @GetMapping("/validateMail")
    public ResponseEntity<ApiResponse> validateEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(new ApiResponse(200,"OK", authServiceImpl.emailValidate(email)));
    }
    @GetMapping("/validateNIC")
    public ResponseEntity<ApiResponse> validateNic(@RequestParam("nic") String nic) {
        return ResponseEntity.ok(new ApiResponse(200,"OK", authServiceImpl.nicValidate(nic)));
    }
}
