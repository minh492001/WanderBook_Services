package com.wander_book.dto.request.branch;

import com.wander_book.dto.request.room.SuperSimpleRoomDTO;
import com.wander_book.dto.request.service.SimpleServiceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchDTO {
    private Long id;
    private String branchName;
    private String city;
    private String address;
    private List<SuperSimpleRoomDTO> rooms;
    private List<SimpleServiceDTO> services;
}
