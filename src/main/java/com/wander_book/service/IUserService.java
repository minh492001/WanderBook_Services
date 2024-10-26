package com.wander_book.service;

import com.wander_book.model.User;
import com.wander_book.request.RegisterRequest;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    // Register a new user
    User registerUser(RegisterRequest registerRequest);

    // Find user by email
    Optional<User> findByEmail(String email);

    // Find user by ID
    Optional<User> findById(Long id);

    // Soft delete user by email
    void deleteByEmail(String email);

    // Soft delete user by ID
    void deleteById(Long id);

    // Count users by age
    long countUsersByAge(int age);

    // Get all users
    List<User> getAllUsers();
}
