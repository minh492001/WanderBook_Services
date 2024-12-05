package com.wander_book.service.impl;

import com.wander_book.dto.request.branch.CreateBranchRequest;
import com.wander_book.dto.request.branch.UpdateBranchRequest;
import com.wander_book.mapper.BranchMapper;
import com.wander_book.model.branch.Branch;
import com.wander_book.repository.BranchRepository;
import com.wander_book.repository.RoomRepository;
import com.wander_book.repository.ServiceProvideRepository;
import com.wander_book.dto.request.branch.BranchDTO;
import com.wander_book.dto.request.branch.BranchNameDTO;
import com.wander_book.service.Common.BaseServiceImpl;
import com.wander_book.service.Common.Utility;
import com.wander_book.service.IBranchService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BranchService extends BaseServiceImpl<Branch> implements IBranchService {

    private final BranchRepository branchRepository;
    private final RoomRepository roomRepository;
    private final ServiceProvideRepository serviceProvideRepository;
    private final BranchMapper branchMapper;

    @Autowired
    public BranchService(BranchRepository branchRepository, RoomRepository roomRepository, ServiceProvideRepository serviceProvideRepository, BranchMapper branchMapper) {
        this.repository = branchRepository;  // Initialize the inherited repository field
        this.branchRepository = branchRepository;
        this.roomRepository = roomRepository;
        this.serviceProvideRepository = serviceProvideRepository;
        this.branchMapper = branchMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchDTO> getAllBranches() {
        List<Branch> branches = branchRepository.findAll();
        return branches.stream()
                .map(branchMapper::toBranchSimpleDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchNameDTO> getAllBranchNames() {
        List<Branch> branches = branchRepository.findByDeletedAtIsNull();
        return branches.stream()
                .map(branch -> new BranchNameDTO(branch.getId(), branch.getBranchName()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BranchDTO getBranchById(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Branch not found with id: " + id));
        return branchMapper.toBranchSimpleDTO(branch);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchDTO> getBranchesByCity(String city) {
        return branchRepository.findByCity(city).stream()
                .map(branchMapper::toBranchSimpleDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BranchDTO addBranch(CreateBranchRequest branchRequest) {
        Branch branch = Branch.builder()
                .branchName(branchRequest.getBranchName())
                .city(branchRequest.getCity())
                .address(branchRequest.getAddress())
                .build();
        Branch savedBranch = branchRepository.save(branch);
        return branchMapper.toBranchSimpleDTO(savedBranch); // Sử dụng mapper để chuyển đổi sang BranchDTO
    }

    public BranchDTO updateBranch(Long id, UpdateBranchRequest updateBranchRequest) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with ID: " + id));

        // Use Utility to update fields selectively
        Utility.updateIfNotNull(updateBranchRequest.getBranchName(), branch::setBranchName);
        Utility.updateIfNotNull(updateBranchRequest.getCity(), branch::setCity);
        Utility.updateIfNotNull(updateBranchRequest.getAddress(), branch::setAddress);

        branchRepository.save(branch);
        return branchMapper.toBranchSimpleDTO(branch);
    }

    @Override
    public void deleteBranchById(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with ID: " + id));
        branchRepository.softDelete(branch);
    }

//    @Override
//    public void addServiceToBranch(Long branchId, Long serviceId) {
//        Branch branch = branchRepository.findById(branchId)
//                .orElseThrow(() -> new IllegalArgumentException("Branch not found"));
//        ServiceProvide service = serviceProvideRepository.findById(serviceId)
//                .orElseThrow(() -> new IllegalArgumentException("Service not found"));
//        branch.getServiceProvides().add(service);
//        branchRepository.save(branch);
//    }
//
//    @Override
//    public void removeServiceFromBranch(Long branchId, Long serviceId) {
//        Branch branch = branchRepository.findById(branchId)
//                .orElseThrow(() -> new IllegalArgumentException("Branch not found"));
//        ServiceProvide service = serviceProvideRepository.findById(serviceId)
//                .orElseThrow(() -> new IllegalArgumentException("Service not found"));
//        branch.getServiceProvides().remove(service);
//        branchRepository.save(branch);
//    }
}
