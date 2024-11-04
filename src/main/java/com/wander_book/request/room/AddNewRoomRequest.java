package com.wander_book.request.room;

import com.wander_book.model.room.RoomType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Blob;

@Data
public class AddNewRoomRequest {

    @NotNull(message = "Branch ID is required")
    private Long branchId;

    @NotNull(message = "Room number is required")
    private String roomNumber;

    @NotNull(message = "Room type is required")
    private RoomType roomType;

    @NotNull(message = "Price per night is required")
    private BigDecimal pricePerNight;

    @NotNull(message = "Max occupancy is required")
    private int maxOccupancy;

    private String description;

    private Blob photo;
}
