package com.wander_book.service.impl;

import com.wander_book.model.Branch;
import com.wander_book.model.ServiceProvide;
import com.wander_book.repository.BranchRepository;
import com.wander_book.service.Common.BaseServiceImpl;
import com.wander_book.service.IBranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BranchService extends BaseServiceImpl<Branch> implements IBranchService {

    private final BranchRepository branchRepository;
//    private final ServiceProvideRepository serviceProvideRepository;

    @Autowired
    public BranchService(BranchRepository branchRepository) {
        this.repository = branchRepository;  // Initialize the inherited repository field
        this.branchRepository = branchRepository;
    }

    @Override
    public Optional<Branch> findByName(String branchName) {
        return Optional.empty();
    }

    @Override
    public boolean existsByName(String branchName) {
        return false;
    }

    @Override
    public List<ServiceProvide> getAllServicesOfBranch(Long branchId) {
        return List.of();
    }

    @Override
    public void addServiceToBranch(Long branchId, ServiceProvide service) {

    }

    @Override
    public void removeServiceFromBranch(Long branchId, ServiceProvide service) {

    }

    @Override
    public List<Branch> findByCity(String city) {
        return List.of();
    }
}
