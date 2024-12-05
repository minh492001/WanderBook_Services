package com.wander_book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingResponseDTO {
    private Long id;
    private String userEmail;
    private String roomNumber;
    private Long checkInTimestamp;
    private Long checkOutTimestamp;
    private int adultsCount;
    private int childrenCount;
    private int totalGuests;
    private String notes;
    private String confirmationCode;
    private String status;
    private List<ServiceProvideResponseDTO> services;
}
