package com.equipsphere.service.impl;

import com.equipsphere.dto.auth.AuthResponseDTO;
import com.equipsphere.dto.auth.ChangePasswordDTO;
import com.equipsphere.dto.auth.LoginRequestDTO;
import com.equipsphere.dto.auth.RegisterRequestDTO;
import com.equipsphere.dto.user.UserResponseDTO;
import com.equipsphere.entity.User;
import com.equipsphere.exception.ResourceNotFoundException;
import com.equipsphere.repository.UserRepository;
import com.equipsphere.security.jwt.JwtUtils;
import com.equipsphere.security.services.UserDetailsImpl;
import com.equipsphere.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Error: Email is already registered!");
        }

        String role = (request.getRole() != null && request.getRole().equalsIgnoreCase("ADMIN")) ? "ADMIN" : "USER";

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail().toLowerCase().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .phone(request.getPhone())
                .department(request.getDepartment())
                .build();

        User savedUser = userRepository.save(user);

        String jwt = jwtUtils.generateTokenFromUser(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getName(),
                savedUser.getRole(),
                savedUser.getDepartment(),
                savedUser.getPhone()
        );

        return AuthResponseDTO.builder()
                .token(jwt)
                .type("Bearer")
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .phone(savedUser.getPhone())
                .department(savedUser.getDepartment())
                .build();
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail().toLowerCase().trim(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return AuthResponseDTO.builder()
                .token(jwt)
                .type("Bearer")
                .id(userDetails.getId())
                .name(userDetails.getName())
                .email(userDetails.getEmail())
                .role(userDetails.getRole())
                .phone(userDetails.getPhone())
                .department(userDetails.getDepartment())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new ResourceNotFoundException("No authenticated user found in current session");
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        return mapToUserResponseDTO(user);
    }

    @Override
    @Transactional
    public UserResponseDTO updateProfile(RegisterRequestDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        if (request.getName() != null) user.setName(request.getName());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getDepartment() != null) user.setDepartment(request.getDepartment());

        User updatedUser = userRepository.save(user);
        return mapToUserResponseDTO(updatedUser);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordDTO changePasswordDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password does not match!");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);
    }

    private UserResponseDTO mapToUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .phone(user.getPhone())
                .department(user.getDepartment())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
