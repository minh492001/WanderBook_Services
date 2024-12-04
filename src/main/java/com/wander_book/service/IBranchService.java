package com.wander_book.service;

import com.wander_book.dto.request.branch.CreateBranchRequest;
import com.wander_book.dto.request.branch.UpdateBranchRequest;
import com.wander_book.model.branch.Branch;
import com.wander_book.dto.request.branch.BranchDTO;
import com.wander_book.dto.request.branch.BranchNameDTO;
import com.wander_book.service.Common.IBaseService;

import java.util.List;

public interface IBranchService extends IBaseService<Branch> {

    List<BranchDTO> getAllBranches();

    List<BranchNameDTO> getAllBranchNames();

    BranchDTO getBranchById(Long id);

    List<BranchDTO> getBranchesByCity(String city);

    void deleteBranchById(Long id);

    BranchDTO addBranch(CreateBranchRequest createBranchRequest);

    BranchDTO updateBranch(Long id, UpdateBranchRequest updateBranchRequest);
//    void addServiceToBranch(Long branchId, Long serviceId);
//
//    void removeServiceFromBranch(Long branchId, Long serviceId);
}
