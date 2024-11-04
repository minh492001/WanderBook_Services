package com.wander_book.controller;

import com.wander_book.model.Branch;
import com.wander_book.model.room.Room;
import com.wander_book.service.IBranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v2/branches")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class BranchController {
    private final IBranchService branchService;

    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("hello");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Branch>> getAllBranches() {
        List<Branch> branches = branchService.findAll();
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Branch> getBranchById(@PathVariable Long id) {
        Optional<Branch> branch = branchService.findByIdAndNotDeleted(id);
        return branch.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<Branch>> getBranchesByCity(@PathVariable String city) {
        List<Branch> branches = branchService.findByCity(city);
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/{branchId}/rooms")
    public ResponseEntity<List<Room>> getRoomsByBranchId(@PathVariable Long branchId) {
        List<Room> rooms = branchService.getRoomsByBranchId(branchId);
        return ResponseEntity.ok(rooms);
    }

    @PostMapping
    public ResponseEntity<Branch> addBranch(@RequestBody Branch branch) {
        Branch savedBranch = branchService.save(branch);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBranch);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranchById(id);
        return ResponseEntity.ok("Branch soft-deleted successfully.");
    }

    @PutMapping("/{branchId}/services/add")
    public ResponseEntity<String> addServiceToBranch(@PathVariable Long branchId, @RequestParam Long serviceId) {
        branchService.addServiceToBranch(branchId, serviceId);
        return ResponseEntity.ok("Service added to branch successfully.");
    }

    @PutMapping("/{branchId}/services/remove")
    public ResponseEntity<String> removeServiceFromBranch(@PathVariable Long branchId, @RequestParam Long serviceId) {
        branchService.removeServiceFromBranch(branchId, serviceId);
        return ResponseEntity.ok("Service removed from branch successfully.");
    }
}
