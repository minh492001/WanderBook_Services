package com.wander_book.controller;

import com.wander_book.dto.request.branch.CreateBranchRequest;
import com.wander_book.dto.request.branch.UpdateBranchRequest;
import com.wander_book.model.branch.Branch;
import com.wander_book.model.room.Room;
import com.wander_book.dto.request.branch.BranchDTO;
import com.wander_book.dto.request.branch.BranchNameDTO;
import com.wander_book.service.IBranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/branches")
@RequiredArgsConstructor
public class BranchController {
    private final IBranchService branchService;

    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("hello");
    }

    @GetMapping("/all")
    public ResponseEntity<List<BranchDTO>> getAllBranches() {
        List<BranchDTO> branches = branchService.getAllBranches();
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchDTO> getBranchById(@PathVariable Long id) {
        BranchDTO branch = branchService.getBranchById(id);
        return ResponseEntity.ok(branch);
    }

    @GetMapping("/names")
    public ResponseEntity<List<BranchNameDTO>> getBranchNames() {
        List<BranchNameDTO> branchNames = branchService.getAllBranchNames();
        return ResponseEntity.ok(branchNames);
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<BranchDTO>> getBranchesByCity(@PathVariable String city) {
        List<BranchDTO> branches = branchService.getBranchesByCity(city);
        return ResponseEntity.ok(branches);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<BranchDTO> addBranch(@RequestBody CreateBranchRequest branchRequest) {
        BranchDTO savedBranch = branchService.addBranch(branchRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBranch);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BranchDTO> updateBranch(@PathVariable Long id, @RequestBody UpdateBranchRequest request) {
        BranchDTO updatedBranch = branchService.updateBranch(id, request);
        return ResponseEntity.ok(updatedBranch);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranchById(id);
        return ResponseEntity.ok("Branch soft-deleted successfully.");
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
//    @PutMapping("/{branchId}/services/add")
//    public ResponseEntity<String> addServiceToBranch(@PathVariable Long branchId, @RequestParam Long serviceId) {
//        branchService.addServiceToBranch(branchId, serviceId);
//        return ResponseEntity.ok("Service added to branch successfully.");
//    }
//
//    @PreAuthorize("hasAuthority('ADMIN')")
//    @PutMapping("/{branchId}/services/remove")
//    public ResponseEntity<String> removeServiceFromBranch(@PathVariable Long branchId, @RequestParam Long serviceId) {
//        branchService.removeServiceFromBranch(branchId, serviceId);
//        return ResponseEntity.ok("Service removed from branch successfully.");
//    }
}
