package com.wander_book.service.impl;

import com.wander_book.exception.auth.UserAlreadyExistsException;
import com.wander_book.model.user.User;
import com.wander_book.model.user.Roles;
import com.wander_book.repository.UserRepository;
import com.wander_book.request.auth.RegisterRequest;
import com.wander_book.request.user.editUserRequest;
import com.wander_book.service.Common.BaseServiceImpl;
import com.wander_book.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService extends BaseServiceImpl<User> implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.repository = userRepository;  // Initialize the inherited repository field
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(RegisterRequest registerRequest) throws UserAlreadyExistsException {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException(registerRequest.getEmail() + " already exists");
        }
        // Create a new user entity
        User newUser = new User(
                registerRequest.getFullName(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getAddress(),
                registerRequest.getPhoneNo(),
                registerRequest.getDateOfBirth(),
                Roles.USER
        );
        userRepository.save(newUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        // Find a user by email
        return userRepository.findByEmail(email);
    }

    @Override
    public void deleteByEmail(String email) {
        // Soft delete a user by email
        userRepository.findByEmail(email).ifPresent(user -> {
            user.onDelete();
            userRepository.save(user);
        });
    }

    @Override
    public void deleteById(Long id) {
        // Soft delete a user by ID
        userRepository.findById(id).ifPresent(user -> {
            user.onDelete();
            userRepository.save(user);
        });
    }

    @Override
    public User updateUser(Long id, editUserRequest updatedUser) {
        return userRepository.findByIdAndDeletedAtIsNull(id).map(existingUser -> {
            if (updatedUser.getFullName() != null) existingUser.setFullName(updatedUser.getFullName());
            if (updatedUser.getEmail() != null) existingUser.setEmail(updatedUser.getEmail());
            if (updatedUser.getAddress() != null) existingUser.setAddress(updatedUser.getAddress());
            if (updatedUser.getPhoneNo() != null) existingUser.setPhoneNo(updatedUser.getPhoneNo());
            if (updatedUser.getDateOfBirth() != null) existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
            if (updatedUser.getPassword() != null)
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword())); // Encode the new password
            existingUser.onUpdate(); // Set updated timestamp
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new IllegalArgumentException("User not found or has been deleted"));
    }

//    @Override
//    public long countUsersByAge(int age) {
//        // Count users by age (assuming age is calculated based on dateOfBirth)
//        long currentTime = System.currentTimeMillis();
//        long ageInMillis = age * 365L * 24 * 60 * 60 * 1000;
//        long ageThreshold = currentTime - ageInMillis;
//        return userRepository.countByDateOfBirthLessThan(ageThreshold);
//    }
}
