package com.wander_book.service;

import com.wander_book.dto.response.UserResponseDTO;
import com.wander_book.model.user.User;
import com.wander_book.dto.request.auth.RegisterRequest;
import com.wander_book.dto.request.auth.ResetPasswordRequest;
import com.wander_book.dto.request.user.editUserRequest;
import com.wander_book.service.Common.IBaseService;

import java.util.List;
import java.util.Optional;

public interface IUserService extends IBaseService<User> {

    void registerUser(RegisterRequest registerRequest);

    List<UserResponseDTO> getAllUsers();

    Optional<UserResponseDTO> findByEmailSimple(String email);

    Optional<User> findByEmail(String email);

    void deleteByEmail(String email);

    void deleteById(Long id);

    UserResponseDTO updateUser(Long id, editUserRequest updatedUser);

    void resetPassword(String email, ResetPasswordRequest resetPasswordRequest);
}
