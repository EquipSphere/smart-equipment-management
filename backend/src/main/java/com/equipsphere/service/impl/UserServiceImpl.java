package com.equipsphere.service.impl;

import com.equipsphere.dto.user.UserResponseDTO;
import com.equipsphere.dto.user.UserUpdateDTO;
import com.equipsphere.entity.User;
import com.equipsphere.exception.ResourceNotFoundException;
import com.equipsphere.repository.UserRepository;
import com.equipsphere.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToDTO(user);
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return mapToDTO(user);
    }

    @Override
    public UserResponseDTO createUser(com.equipsphere.dto.user.UserCreateDTO createDTO) {
        if (userRepository.existsByEmail(createDTO.getEmail())) {
            throw new IllegalArgumentException("User with email '" + createDTO.getEmail() + "' already exists.");
        }

        User user = User.builder()
                .name(createDTO.getName())
                .email(createDTO.getEmail())
                .password(createDTO.getPassword())
                .role(createDTO.getRole() != null ? createDTO.getRole() : "USER")
                .phone(createDTO.getPhone())
                .department(createDTO.getDepartment())
                .build();

        User saved = userRepository.save(user);
        return mapToDTO(saved);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserUpdateDTO updateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setName(updateDTO.getName());
        user.setPhone(updateDTO.getPhone());
        user.setDepartment(updateDTO.getDepartment());
        if (updateDTO.getRole() != null && !updateDTO.getRole().trim().isEmpty()) {
            user.setRole(updateDTO.getRole());
        }

        User updatedUser = userRepository.save(user);
        return mapToDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }

    @Override
    public List<UserResponseDTO> searchUsers(String keyword) {
        return userRepository.searchUsers(keyword).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDTO> getUsersByRole(String role) {
        return userRepository.findByRole(role).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private UserResponseDTO mapToDTO(User user) {
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
