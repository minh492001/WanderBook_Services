package com.wander_book.controller;

import com.wander_book.model.user.ForgotPassword;
import com.wander_book.model.user.User;
import com.wander_book.repository.ForgotPasswordRepository;
import com.wander_book.dto.request.auth.ResetPasswordRequest;
import com.wander_book.dto.response.MailBody;
import com.wander_book.service.IEmailService;
import com.wander_book.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/api/v2/forgot-password")
@RequiredArgsConstructor
public class ForgotPasswordController {
    private final IUserService userService;
    private final IEmailService emailService;
    private final ForgotPasswordRepository forgotPasswordRepository;

    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("hello");
    }

    //Send mail for email verification
    @PostMapping("/verify-mail/{email}")
    public ResponseEntity<String> verifyMail(@PathVariable String email) {
        User user = userService.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found, please provide valid email"));

        int otp = otpGenerator();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .subject("Verification Code")
                .content("Verification Code for your Grand Lusso account: " + otp)
                .build();
        ForgotPassword forgotPassword = ForgotPassword.builder()
                .otp(otp)
                .expiryDate(new Date(System.currentTimeMillis() + 700 * 1000)) // 70s
                .user(user)
                .build();
        emailService.sendVerificationCode(mailBody);
        forgotPasswordRepository.save(forgotPassword);
        return ResponseEntity.ok("Email sent for verification: " + mailBody.toString());
    }

    @PutMapping("/verify-and-change-password/{email}")
    public ResponseEntity<String> verifyAndChangePassword(@RequestBody ResetPasswordRequest changePassword,
                                                          @PathVariable String email,
                                                          @RequestParam Integer otp) {
        // Verify user
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found, please provide valid email"));

        // Verify OTP in forgotPassword table
        ForgotPassword forgotPassword = forgotPasswordRepository.findByOtpAndUser(otp, user)
                .orElseThrow(() -> new RuntimeException("Invalid OTP or email. Please try again."));

        // Verify OTP expiry
        if (forgotPassword.getExpiryDate().before(Date.from(Instant.now()))) {
            forgotPasswordRepository.deleteById(forgotPassword.getId());
            return new ResponseEntity<>("OTP has expired!", HttpStatus.EXPECTATION_FAILED);
        }

        // Verify confirmation password
        if (!Objects.equals(changePassword.newPassword(), changePassword.confirmPassword())) {
            return new ResponseEntity<>("Password and Confirm Password do not match", HttpStatus.BAD_REQUEST);
        }

        // Change password
        userService.resetPassword(user.getEmail(), changePassword);

        // Delete OTP to prevent using OTP more than once
        forgotPasswordRepository.deleteById(forgotPassword.getId());

        return ResponseEntity.ok("OTP verified and password changed successfully");
    }

    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}
