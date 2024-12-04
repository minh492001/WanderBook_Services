package com.wander_book.dto.request.auth;

import lombok.Builder;

@Builder
public record ResetPasswordRequest(String newPassword, String confirmPassword ) {}
