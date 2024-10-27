package com.wander_book.repository;

import com.wander_book.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    Optional<Branch> findByBranchName(String branchName);
    boolean existsByBranchName(String branchName);
    List<Branch> findByCity(String city);
    Optional<Branch> findById(Long branchId);
}
