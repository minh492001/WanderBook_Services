package com.wander_book.service.impl;

import com.wander_book.dto.response.UserResponseDTO;
import com.wander_book.exception.auth.UserAlreadyExistsException;
import com.wander_book.mapper.UserMapper;
import com.wander_book.model.user.User;
import com.wander_book.model.user.Roles;
import com.wander_book.repository.UserRepository;
import com.wander_book.dto.request.auth.RegisterRequest;
import com.wander_book.dto.request.auth.ResetPasswordRequest;
import com.wander_book.dto.request.user.editUserRequest;
import com.wander_book.service.Common.BaseServiceImpl;
import com.wander_book.service.Common.Utility;
import com.wander_book.service.IUserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService extends BaseServiceImpl<User> implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.repository = userRepository;  // Initialize the inherited repository field
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public void registerUser(RegisterRequest registerRequest) throws UserAlreadyExistsException {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException(registerRequest.getEmail() + " already exists");
        }

        User newUser = User.builder()
                .fullName(registerRequest.getFullName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .address(registerRequest.getAddress())
                .phoneNo(registerRequest.getPhoneNo())
                .dateOfBirth(registerRequest.getDateOfBirth())
                .role(Roles.USER)
                .build();

        userRepository.save(newUser);
    }

    @Override
    public Optional<UserResponseDTO> findByEmailSimple(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDTO);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void deleteByEmail(String email) {
        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email: " + email + " not found or already deleted!"));
        userRepository.delete(user);
    }

    @Override
    public void deleteById(Long id) {
        User user = userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found or already deleted!"));
        userRepository.delete(user);
    }

    @Override
    public UserResponseDTO updateUser(Long id, editUserRequest updatedUser) {
        return userRepository.findByIdAndDeletedAtIsNull(id).map(existingUser -> {
            Utility.updateIfNotNull(updatedUser.getFullName(), existingUser::setFullName);
            Utility.updateIfNotNull(updatedUser.getEmail(), existingUser::setEmail);
            Utility.updateIfNotNull(updatedUser.getAddress(), existingUser::setAddress);
            Utility.updateIfNotNull(updatedUser.getPhoneNo(), existingUser::setPhoneNo);
            Utility.updateIfNotNull(updatedUser.getDateOfBirth(), existingUser::setDateOfBirth);

            if (updatedUser.getPassword() != null) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            User savedUser = userRepository.save(existingUser);

            return userMapper.toDTO(savedUser);
        }).orElseThrow(() -> new IllegalArgumentException("User not found or has been deleted"));
    }

    @Override
    public void resetPassword(String email, ResetPasswordRequest resetPasswordRequest) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with Email: " + email));

        if (!resetPasswordRequest.newPassword().equals(resetPasswordRequest.confirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password and confirm password do not match");
        }

        existingUser.setPassword(passwordEncoder.encode(resetPasswordRequest.newPassword()));
        userRepository.save(existingUser);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }
}
