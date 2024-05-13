package com.wander_book.service;

import com.wander_book.model.Users;

import java.util.List;

public interface IUserService {
    Users registerUser(Users user);
    List<Users> getUsers();
    void deleteUser(String email);
    Users getUser(String email);
}
