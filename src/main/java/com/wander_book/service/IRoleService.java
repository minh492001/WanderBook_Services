package com.wander_book.service;

import com.wander_book.model.Role;
import com.wander_book.model.Users;

import java.util.List;

public interface IRoleService {
    List<Role> getRoles();
    Role createRole(Role theRole);

    void deleteRole(Long id);
    Role findByName(String name);

    Users removeUserFromRole(Long userId, Long roleId);
    Users assignRoleToUser(Long userId, Long roleId);
    Role removeAllUsersFromRole(Long roleId);
}
