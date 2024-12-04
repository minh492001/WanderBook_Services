package com.wander_book.dto.request;

import com.wander_book.model.booking.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimpleBookingRequest {
    private Long userId;
    private Long roomId;
    private Long checkInTimestamp;
    private Long checkOutTimestamp;
    private int adultsCount;
    private int childrenCount;
    private int totalGuests;
    private String notes;
    private BookingStatus status;
}
