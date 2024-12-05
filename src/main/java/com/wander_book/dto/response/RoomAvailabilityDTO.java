package com.wander_book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomAvailabilityDTO {
    private Long id;
    private Long roomId;
    private Long startDate;
    private Long endDate;
}
