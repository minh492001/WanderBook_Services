package com.wander_book.service.impl;

import com.wander_book.exception.UserAlreadyExistsException;
import com.wander_book.model.User;
import com.wander_book.model.enums.Roles;
import com.wander_book.repository.UserRepository;
import com.wander_book.request.RegisterRequest;
import com.wander_book.service.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(RegisterRequest registerRequest) throws UserAlreadyExistsException {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException(registerRequest.getEmail() + " already exists");
        }

        // Create a new user entity
        User newUser = new User(
                registerRequest.getFullName(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getAddress(),
                registerRequest.getDateOfBirth(),
                Roles.USER
        );
        return userRepository.save(newUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        // Find a user by email
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        // Find a user by ID
        return userRepository.findById(id);
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
    public long countUsersByAge(int age) {
        // Count users by age (assuming age is calculated based on dateOfBirth)
        long currentTime = System.currentTimeMillis();
        long ageInMillis = age * 365L * 24 * 60 * 60 * 1000;
        long ageThreshold = currentTime - ageInMillis;
        return userRepository.countByDateOfBirthLessThan(ageThreshold);
    }

    @Override
    public List<User> getAllUsers() {
        // Return all users in the system
        return userRepository.findAll();
    }
}
