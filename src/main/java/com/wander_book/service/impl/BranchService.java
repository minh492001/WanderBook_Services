package com.wander_book.service.impl;

import com.wander_book.model.Branch;
import com.wander_book.model.room.Room;
import com.wander_book.model.service_provide.ServiceProvide;
import com.wander_book.repository.BranchRepository;
import com.wander_book.repository.RoomRepository;
import com.wander_book.repository.ServiceProvideRepository;
import com.wander_book.service.Common.BaseServiceImpl;
import com.wander_book.service.IBranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BranchService extends BaseServiceImpl<Branch> implements IBranchService {

    private final BranchRepository branchRepository;
    private final RoomRepository roomRepository;
    private final ServiceProvideRepository serviceProvideRepository;

    @Autowired
    public BranchService(BranchRepository branchRepository, RoomRepository roomRepository, ServiceProvideRepository serviceProvideRepository) {
        this.repository = branchRepository;  // Initialize the inherited repository field
        this.branchRepository = branchRepository;
        this.roomRepository = roomRepository;
        this.serviceProvideRepository = serviceProvideRepository;
    }

    @Override
    public Optional<Branch> findByName(String branchName) {
        return branchRepository.findByBranchName(branchName);
    }

    @Override
    public boolean existsByName(String branchName) {
        return branchRepository.existsByBranchName(branchName);
    }

    @Override
    public List<Branch> findByCity(String city) {
        return branchRepository.findByCity(city);
    }

    @Override
    public Branch save(Branch branch) {
        return branchRepository.save(branch);
    }

    @Override
    public void deleteBranchById(Long id) {
        branchRepository.findById(id).ifPresent(branchRepository::softDelete);
    }

    @Override
    public void addServiceToBranch(Long branchId, Long serviceId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new IllegalArgumentException("Branch not found"));
        ServiceProvide service = serviceProvideRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));
        branch.getServiceProvides().add(service);
        branchRepository.save(branch);
    }

    @Override
    public void removeServiceFromBranch(Long branchId, Long serviceId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new IllegalArgumentException("Branch not found"));
        ServiceProvide service = serviceProvideRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));
        branch.getServiceProvides().remove(service);
        branchRepository.save(branch);
    }

    @Override
    public List<Room> getRoomsByBranchId(Long branchId) {
        return roomRepository.findByBranchId(branchId);
    }
}
