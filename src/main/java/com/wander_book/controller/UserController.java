package com.wander_book.controller;

import com.wander_book.model.user.User;
import com.wander_book.request.user.editUserRequest;
import com.wander_book.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v2/user")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("hello");
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            Optional<User> user = userService.findByIdAndNotDeleted(id);
            return ResponseEntity.ok(user);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user");
        }
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<?> getUserByEmail(@PathVariable("email") String email) {
        try {
            Optional<User> user = userService.findByEmail(email);
            return ResponseEntity.ok(user);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> softDeleteUserById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok("User soft deleted by id successfully.");
    }

    @DeleteMapping("/email/{email}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> softDeleteUserByEmail(@PathVariable("email") String email) {
        userService.deleteByEmail(email);
        return ResponseEntity.ok("User soft deleted successfully by email.");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody editUserRequest updatedUser) {
        try {
            User updated = userService.updateUser(id, updatedUser);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while updating the user");
        }
    }

//    @GetMapping("/count-by-age")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public ResponseEntity<Long> countUsersByAge(@RequestParam int age) {
//        long count = userService.countUsersByAge(age);
//        return ResponseEntity.ok(count);
//    }
}
