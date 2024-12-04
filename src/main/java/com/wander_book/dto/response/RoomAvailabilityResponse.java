package com.wander_book.dto.response;

import lombok.Data;

@Data
public class RoomAvailabilityResponse {
    private Long id;
    private Long startDate;
    private Long endDate;
}
