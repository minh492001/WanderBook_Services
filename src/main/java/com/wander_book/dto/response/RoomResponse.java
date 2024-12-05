package com.wander_book.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RoomResponse {
    private Long id;
    private String roomNumber;
    private String roomType;
    private BigDecimal pricePerNight;
    private int maxOccupancy;
    private String state;
    private List<RoomAvailabilityDTO> futureBookings;
}
