package com.wander_book.controller;

import com.wander_book.exception.RoleAlreadyExistsException;
import com.wander_book.model.Role;
import com.wander_book.model.Users;
import com.wander_book.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.FOUND;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final IRoleService roleService;

    @GetMapping("/all-roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return new ResponseEntity<>(roleService.getRoles(), FOUND);
    }

    @PostMapping("/create-new-role")
    public ResponseEntity<String> createRole(@RequestBody Role role) {
        try {
            roleService.createRole(role);
            return ResponseEntity.ok("New role created successfully !");
        } catch (RoleAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{roleId}")
    public void deleteRole(@PathVariable("roleId") Long roleId) {
        roleService.deleteRole(roleId);
    }

    @PostMapping("/remove-all-user-from-role/{roleId}")
    public Role removeAllUsersFromRole(@PathVariable("roleId") Long roleId) {
        return roleService.removeAllUsersFromRole(roleId);
    }

    @PostMapping("/remove-user-from-role")
    public Users removeUserFromRole(@RequestParam("userId") Long userId, @RequestParam("roleId") Long roleId) {
        return roleService.removeUserFromRole(userId, roleId);
    }

    @PostMapping("/assign-user-to-role")
    public Users assignUserToRole(@RequestParam("userId") Long userId, @RequestParam("roleId") Long roleId) {
        return roleService.assignRoleToUser(userId, roleId);
    }
}
