package com.wander_book.model.user;

import com.wander_book.model.comon.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    private String fullName;
    private String email;
    private String password;
    private String address;
    private String phoneNo;
    private Long dateOfBirth; // (UNIX timestamp)

    @Enumerated(EnumType.STRING)
    private Roles role;

    public User() {
        super();
    }

    public User(
            String fullName,
            String email,
            String password,
            String address,
            String phoneNo,
            Long dateOfBirth,
            Roles role) {
        super();
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phoneNo = phoneNo;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
    }
}


