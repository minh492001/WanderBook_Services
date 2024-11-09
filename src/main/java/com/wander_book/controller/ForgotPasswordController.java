package com.wander_book.controller;

import com.wander_book.model.auth.ForgotPassword;
import com.wander_book.model.user.User;
import com.wander_book.repository.ForgotPasswordRepository;
import com.wander_book.request.auth.ResetPasswordRequest;
import com.wander_book.response.MailBody;
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
                .expiryDate(new Date(System.currentTimeMillis() + 70 * 1000))
                .user(user)
                .build();
        emailService.sendVerificationCode(mailBody);
        forgotPasswordRepository.save(forgotPassword);
        return ResponseEntity.ok("Email sent for verification: " + mailBody.toString());
    }

//    @PostMapping("/verify-otp/{otp}/{email}")
//    public ResponseEntity<String> verifyOTP(@PathVariable Integer otp, @PathVariable String email) {
//        User user = userService.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found, please provide valid email"));
//        ForgotPassword forgotPassword = forgotPasswordRepository.findByOtpAndUser(otp, user).orElseThrow(() -> new RuntimeException("User Not Found, please provide valid otp"));
//
//        if (forgotPassword.getExpiryDate().before(Date.from(Instant.now()))) {
//            forgotPasswordRepository.deleteById(forgotPassword.getId());
//
//            return new ResponseEntity<>("OTP has expired!", HttpStatus.EXPECTATION_FAILED);
//        }
//
//        return ResponseEntity.ok("OTP verified");
//    }
//
//    @PutMapping("/changePassword/{email}")
//    public ResponseEntity<String> changePassword(@RequestBody ResetPasswordRequest changePassword,
//                                                 @PathVariable String email) {
//        User existingUser = userService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found, please provide valid email"));
//        userService.resetPassword(existingUser.getId(), changePassword);
//
//        return ResponseEntity.ok("Password changed successfully");
//    }

    @PutMapping("/verify-and-change-password/{email}")
    public ResponseEntity<String> verifyAndChangePassword(@RequestBody ResetPasswordRequest changePassword,
                                                          @PathVariable String email,
                                                          @RequestParam Integer otp) {
        // Kiểm tra xem người dùng có tồn tại không
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found, please provide valid email"));

        // Kiểm tra OTP và người dùng trong bảng ForgotPassword
        ForgotPassword forgotPassword = forgotPasswordRepository.findByOtpAndUser(otp, user)
                .orElseThrow(() -> new RuntimeException("Invalid OTP or email. Please try again."));

        // Kiểm tra xem OTP đã hết hạn chưa
        if (forgotPassword.getExpiryDate().before(Date.from(Instant.now()))) {
            forgotPasswordRepository.deleteById(forgotPassword.getId());
            return new ResponseEntity<>("OTP has expired!", HttpStatus.EXPECTATION_FAILED);
        }

        // Xác minh mật khẩu mới và xác nhận khớp
        if (!Objects.equals(changePassword.newPassword(), changePassword.confirmPassword())) {
            return new ResponseEntity<>("Password and Confirm Password do not match", HttpStatus.BAD_REQUEST);
        }

        // Thay đổi mật khẩu nếu OTP hợp lệ
        userService.resetPassword(user.getId(), changePassword);

        // Xóa mã OTP sau khi hoàn thành để ngăn chặn việc sử dụng lại
        forgotPasswordRepository.deleteById(forgotPassword.getId());

        return ResponseEntity.ok("OTP verified and password changed successfully");
    }

    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}
