package com.wander_book.mapper;

import com.wander_book.dto.request.room.AddNewRoomRequest;
import com.wander_book.dto.request.room.RoomDetailsDTO;
import com.wander_book.dto.request.room.SimpleRoomDTO;
import com.wander_book.model.branch.Branch;
import com.wander_book.model.room.Room;
import com.wander_book.dto.response.RoomResponse;
import com.wander_book.model.room.RoomState;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomMapper {
    private final RoomAvailabilityMapper roomAvailabilityMapper;

    public RoomMapper(RoomAvailabilityMapper roomAvailabilityMapper) {
        this.roomAvailabilityMapper = roomAvailabilityMapper;
    }

    public RoomResponse toDto(Room room) {
        RoomResponse dto = new RoomResponse();
        dto.setId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setRoomType(room.getRoomType().name());
        dto.setMaxOccupancy(room.getMaxOccupancy());
        dto.setFutureBookings(
                room.getFutureBookings() != null
                        ? room.getFutureBookings().stream()
                        .map(roomAvailabilityMapper::toDto)
                        .collect(Collectors.toList())
                        : List.of()
        );
        return dto;
    }

    public SimpleRoomDTO toSimpleRoomDTO(Room room) {
        return SimpleRoomDTO.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType())
                .pricePerNight(room.getPricePerNight())
                .state(room.getState())
                .maxOccupancy(room.getMaxOccupancy())
                .branchName(room.getBranch().getBranchName())
                .build();
    }

    public RoomDetailsDTO toRoomDetailsDTO(Room room) {
        return RoomDetailsDTO.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType())
                .pricePerNight(room.getPricePerNight())
                .state(room.getState())
                .maxOccupancy(room.getMaxOccupancy())
                .branchId(room.getBranch() != null ? room.getBranch().getId() : null)
                .description(room.getDescription())
                .photo(room.getPhoto() != null ? Base64.getEncoder().encodeToString(room.getPhoto()) : null) // Encode photo
                .build();
    }

    public Room toRoom(AddNewRoomRequest dto, Branch branch) {
        return Room.builder()
                .roomNumber(dto.getRoomNumber())
                .roomType(dto.getRoomType())
                .pricePerNight(dto.getPricePerNight())
                .maxOccupancy(dto.getMaxOccupancy())
                .description(dto.getDescription())
                .photo(dto.getPhoto() != null ? Base64.getDecoder().decode(dto.getPhoto()) : null) // Decode photo
                .state(RoomState.OPEN)
                .branch(branch)
                .build();
    }
}
