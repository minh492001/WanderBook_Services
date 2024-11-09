package com.wander_book.request.auth;

import lombok.Builder;

@Builder
public record ResetPasswordRequest(String newPassword, String confirmPassword ) {}
