package com.wander_book.mapper;

import com.wander_book.model.Branch;
import com.wander_book.request.branch.BranchDTO;
import com.wander_book.request.room.SimpleRoomDTO;
import com.wander_book.request.service.SimpleServiceDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BranchMapper {
    public BranchDTO toBranchDTO(Branch branch) {
        BranchDTO branchDTO = new BranchDTO();
        branchDTO.setId(branch.getId());
        branchDTO.setBranchName(branch.getBranchName());
        branchDTO.setCity(branch.getCity());
        branchDTO.setAddress(branch.getAddress());

        branchDTO.setRooms(branch.getRooms().stream()
                .map(room -> new SimpleRoomDTO(room.getId(), room.getRoomNumber())).collect(Collectors.toList()));

        branchDTO.setServices(branch.getServiceProvides().stream()
                .map(service -> new SimpleServiceDTO(service.getId(), service.getServiceName())).collect(Collectors.toList()));

        return branchDTO;
    }
}
