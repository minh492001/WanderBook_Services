package com.wander_book.dto.request.booking;

import com.wander_book.model.booking.BookingStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequestDTO {
    @NotNull(message = "User ID is required.")
    private Long userId;

    @NotNull(message = "Room ID is required.")
    private Long roomId;

    @NotNull(message = "Check-in timestamp is required.")
    private Long checkInTimestamp;

    @NotNull(message = "Check-out timestamp is required.")
    private Long checkOutTimestamp;

    @NotNull(message = "Adults count is required.")
    @Min(value = 1, message = "There must be at least one adult.")
    private int adultsCount;
    private int childrenCount;
    private int totalGuests;
    private String notes;
    private List<Long> serviceIds;
    private BookingStatus status;
}
