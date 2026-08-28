package com.equipsphere.controller;

import com.equipsphere.dto.auth.AuthResponseDTO;
import com.equipsphere.dto.auth.ChangePasswordDTO;
import com.equipsphere.dto.auth.LoginRequestDTO;
import com.equipsphere.dto.auth.RegisterRequestDTO;
import com.equipsphere.dto.user.UserResponseDTO;
import com.equipsphere.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        AuthResponseDTO response = authService.register(registerRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        AuthResponseDTO response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser() {
        UserResponseDTO response = authService.getCurrentUser();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponseDTO> updateProfile(@RequestBody RegisterRequestDTO updateRequest) {
        UserResponseDTO response = authService.updateProfile(updateRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        authService.changePassword(changePasswordDTO);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password changed successfully!");
        return ResponseEntity.ok(response);
    }
}
