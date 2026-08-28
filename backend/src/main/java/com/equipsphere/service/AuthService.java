package com.equipsphere.service;

import com.equipsphere.dto.auth.AuthResponseDTO;
import com.equipsphere.dto.auth.ChangePasswordDTO;
import com.equipsphere.dto.auth.LoginRequestDTO;
import com.equipsphere.dto.auth.RegisterRequestDTO;
import com.equipsphere.dto.user.UserResponseDTO;

public interface AuthService {
    AuthResponseDTO register(RegisterRequestDTO registerRequest);
    AuthResponseDTO login(LoginRequestDTO loginRequest);
    UserResponseDTO getCurrentUser();
    UserResponseDTO updateProfile(RegisterRequestDTO updateRequest);
    void changePassword(ChangePasswordDTO changePasswordDTO);
}
