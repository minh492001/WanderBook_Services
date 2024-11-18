package com.wander_book.mapper;

import com.wander_book.model.room.Room;
import com.wander_book.model.room.RoomAvailability;
import com.wander_book.response.RoomAvailabilityResponse;
import com.wander_book.response.RoomResponse;
import org.springframework.stereotype.Component;

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
}
