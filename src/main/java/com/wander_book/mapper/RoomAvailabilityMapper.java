package com.wander_book.mapper;

import com.wander_book.dto.response.RoomAvailabilityDTO;
import com.wander_book.model.room.RoomAvailability;
import org.springframework.stereotype.Component;

@Component
public class RoomAvailabilityMapper {
    public RoomAvailabilityDTO toDto(RoomAvailability availability) {
        return RoomAvailabilityDTO.builder()
                .id(availability.getId())
                .roomId(availability.getRoom().getId())
                .startDate(availability.getStartDate())
                .endDate(availability.getEndDate())
                .build();
    }
}
