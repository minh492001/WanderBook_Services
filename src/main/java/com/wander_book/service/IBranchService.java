package com.wander_book.service;

import com.wander_book.model.Branch;
import com.wander_book.model.room.Room;
import com.wander_book.service.Common.IBaseService;

import java.util.List;
import java.util.Optional;

public interface IBranchService extends IBaseService<Branch> {
    Optional<Branch> findByName(String branchName);


    boolean existsByName(String branchName);

    List<Branch> findByCity(String city);

    List<Room> getRoomsByBranchId(Long branchId);

    void deleteBranchById(Long id);

    void addServiceToBranch(Long branchId, Long serviceId);

    void removeServiceFromBranch(Long branchId, Long serviceId);
}
