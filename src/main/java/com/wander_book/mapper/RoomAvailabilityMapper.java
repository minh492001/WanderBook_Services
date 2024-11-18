package com.wander_book.mapper;

import com.wander_book.model.room.RoomAvailability;
import com.wander_book.response.RoomAvailabilityResponse;
import org.springframework.stereotype.Component;

@Component
public class RoomAvailabilityMapper {
    public RoomAvailabilityResponse toDto(RoomAvailability roomAvailability) {
        RoomAvailabilityResponse dto = new RoomAvailabilityResponse();
        dto.setStartDate(roomAvailability.getStartDate());
        dto.setEndDate(roomAvailability.getEndDate());
        dto.setId(roomAvailability.getBooking() != null ? roomAvailability.getBooking().getId() : null);
        return dto;
    }
}
