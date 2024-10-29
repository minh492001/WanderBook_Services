package com.wander_book.security.user;

import com.wander_book.model.user.User;
import com.wander_book.model.user.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HotelUserDetails implements UserDetails{
    private Long id;
    private String email;
    private String password;
    private GrantedAuthority authorities;
    private Roles role;


    public static HotelUserDetails buildUserDetails(User user) {
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name()); // Assuming role is an enum
        return new HotelUserDetails(user.getId(), user.getEmail(), user.getPassword(), authority, user.getRole());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name())); // Assumes 'role' is an enum in HotelUserDetails

    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
