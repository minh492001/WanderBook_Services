package com.wander_book.request.branch;

import com.wander_book.request.room.SimpleRoomDTO;
import com.wander_book.request.service.SimpleServiceDTO;
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
    private List<SimpleRoomDTO> rooms;
    private List<SimpleServiceDTO> services;
}
