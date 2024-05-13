package com.wander_book.service.impl;

import com.wander_book.exception.UserAlreadyExistsException;
import com.wander_book.model.Role;
import com.wander_book.model.Users;
import com.wander_book.repository.RoleRepository;
import com.wander_book.repository.UserRepository;
import com.wander_book.service.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    @Override
    public Users registerUser(Users user) {
        if (userRepository.existsByEmail(user.getEmail())){
            throw new UserAlreadyExistsException(user.getEmail() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(user.getPassword());
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        user.setRoles(Collections.singletonList(userRole));
        return userRepository.save(user);
    }

    @Override
    public List<Users> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteUser(String email) {
        Users theUsers = getUser(email);
        if (theUsers != null) {
            userRepository.deleteByEmail(email);
        }
    }

    @Override
    public Users getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found !"));
    }
}
