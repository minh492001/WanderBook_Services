package com.wander_book.response;

import lombok.Data;

@Data
public class RoomAvailabilityResponse {
    private Long id;
    private Long startDate;
    private Long endDate;
}
