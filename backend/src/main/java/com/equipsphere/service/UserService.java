package com.equipsphere.service;

import com.equipsphere.dto.user.UserCreateDTO;
import com.equipsphere.dto.user.UserResponseDTO;
import com.equipsphere.dto.user.UserUpdateDTO;

import java.util.List;

public interface UserService {
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(Long id);
    UserResponseDTO getUserByEmail(String email);
    UserResponseDTO createUser(UserCreateDTO createDTO);
    UserResponseDTO updateUser(Long id, UserUpdateDTO updateDTO);
    void deleteUser(Long id);
    List<UserResponseDTO> searchUsers(String keyword);
    List<UserResponseDTO> getUsersByRole(String role);
}
