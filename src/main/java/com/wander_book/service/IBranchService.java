package com.wander_book.service;

import com.wander_book.model.Branch;
import com.wander_book.model.ServiceProvide;
import com.wander_book.service.Common.IBaseService;

import java.util.List;
import java.util.Optional;

public interface IBranchService extends IBaseService<Branch> {
    Optional<Branch> findByName(String branchName);

    boolean existsByName(String branchName);

    List<ServiceProvide> getAllServicesOfBranch(Long branchId);

    void addServiceToBranch(Long branchId, ServiceProvide service);

    void removeServiceFromBranch(Long branchId, ServiceProvide service);

    List<Branch> findByCity(String city);
}
