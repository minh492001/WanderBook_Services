package com.wander_book.service;

import com.wander_book.model.User;
import com.wander_book.request.auth.RegisterRequest;
import com.wander_book.request.user.editUserRequest;
import com.wander_book.service.Common.IBaseService;

import java.util.Optional;

public interface IUserService extends IBaseService<User> {
    // Register a new user
    void registerUser(RegisterRequest registerRequest);

    // Find user by email
    Optional<User> findByEmail(String email);

    // Soft delete user by email
    void deleteByEmail(String email);

    // Soft delete user by ID
    void deleteById(Long id);

    User updateUser(Long id, editUserRequest updatedUser);

    // Count users by age
    long countUsersByAge(int age);

}
