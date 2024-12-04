package com.wander_book.controller;

import com.wander_book.dto.response.UserResponseDTO;
import com.wander_book.model.user.User;
import com.wander_book.dto.request.auth.ResetPasswordRequest;
import com.wander_book.dto.request.user.editUserRequest;
import com.wander_book.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
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
    public ResponseEntity<User> getUserByEmail(@PathVariable("email") String email, Authentication authentication) {

        String tokenEmail = authentication.getName();

        if (!email.equals(tokenEmail) && !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to access this resource, first login to the account with email " + email);
        }

        User user = userService.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> softDeleteUserById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok("User deleted by id successfully.");
    }

    @DeleteMapping("/email/{email}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> softDeleteUserByEmail(@PathVariable("email") String email) {
        userService.deleteByEmail(email);
        return ResponseEntity.ok("User deleted successfully by email.");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody editUserRequest updatedUser) {
        try {
            UserResponseDTO updated = userService.updateUser(id, updatedUser);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while updating the user");
        }
    }

    @PutMapping("/changePassword/{email}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<String> changePassword(@RequestBody ResetPasswordRequest changePassword,
                                                 @PathVariable String email) {
        // Check if the user exists by email
        Optional<User> verifyEmail = userService.findByEmail(email);
        if (verifyEmail.isEmpty()) {
            return new ResponseEntity<>("User doesn't exist or has been deleted!", HttpStatus.NOT_FOUND);
        }

        // Check if the user exists and is not deleted
        Optional<User> existingUser = userService.findByIdAndNotDeleted(verifyEmail.get().getId());
        if (existingUser.isEmpty()) {
            return new ResponseEntity<>("User doesn't exist or has been deleted!", HttpStatus.NOT_FOUND);
        }

        // Reset password
        userService.resetPassword(existingUser.get().getEmail(), changePassword);

        return ResponseEntity.ok("Password changed successfully");
    }
}
