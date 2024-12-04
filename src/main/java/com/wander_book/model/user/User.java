package com.wander_book.model.user;

import com.wander_book.model.comon.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity {

    private String fullName;
    private String email;
    private String password;
    private String address;
    private String phoneNo;
    private Long dateOfBirth;

    @OneToOne(mappedBy = "user")
    private ForgotPassword forgotPassword;

    @Enumerated(EnumType.STRING)
    private Roles role;
}


