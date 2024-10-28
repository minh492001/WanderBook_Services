package com.wander_book.repository;

import com.wander_book.model.Branch;
import com.wander_book.repository.comon.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BranchRepository extends BaseRepository<Branch> {
    Optional<Branch> findByBranchName(String branchName);
    boolean existsByBranchName(String branchName);
    List<Branch> findByCity(String city);
}
