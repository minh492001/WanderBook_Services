package com.wander_book.request.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class editUserRequest {
    private String fullName;
    private String email;
    private String password;
    private String address;
    private String phoneNo;
    private Long dateOfBirth;
}
