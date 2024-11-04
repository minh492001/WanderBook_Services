package com.wander_book.request.room;

import com.wander_book.model.room.RoomState;
import com.wander_book.model.room.RoomType;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Blob;

@Data
public class RoomUpdateRequest {
    private String roomNumber;
    private RoomType roomType;
    private BigDecimal pricePerNight;
    private Integer maxOccupancy;
    private String description;
    private RoomState state;
    private Blob photo;
}
