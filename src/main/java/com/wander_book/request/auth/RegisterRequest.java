package com.wander_book.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Full name must not be blank")
    @Size(min = 3, max = 50)
    private String fullName;

    @NotBlank(message = "Email must not be blank")
    @Email
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, max = 50)
    private String password;

    private String address;
    private String phoneNo;
    private Long dateOfBirth; // Assume this is a UNIX timestamp in milliseconds

    @NotBlank(message = "Confirm password must not be blank")
    private String confirmPassword;
}
