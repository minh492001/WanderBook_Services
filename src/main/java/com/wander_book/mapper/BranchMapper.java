package com.wander_book.mapper;

import com.wander_book.model.branch.Branch;
import com.wander_book.dto.request.branch.BranchDTO;
import com.wander_book.dto.request.room.SuperSimpleRoomDTO;
import com.wander_book.dto.request.service.SimpleServiceDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BranchMapper {
    public BranchDTO toBranchSimpleDTO(Branch branch) {
        BranchDTO branchDTO = new BranchDTO();
        branchDTO.setId(branch.getId());
        branchDTO.setBranchName(branch.getBranchName());
        branchDTO.setCity(branch.getCity());
        branchDTO.setAddress(branch.getAddress());

        branchDTO.setRooms(branch.getRooms().stream()
                .map(room -> new SuperSimpleRoomDTO(room.getId(), room.getRoomNumber()))
                .collect(Collectors.toList()));

        branchDTO.setServices(branch.getServiceProvides().stream()
                .map(service -> new SimpleServiceDTO(service.getId(), service.getServiceName()))
                .collect(Collectors.toList()));

        return branchDTO;
    }
}
