package com.wander_book.dto.response;

import com.wander_book.model.user.Roles;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private String fullName;
    private String email;
    private String address;
    private String phoneNo;
    private Roles role;
    private Long dateOfBirth;
    private Long createdAt;
    private Long updatedAt;
    private Long deletedAt;
}
