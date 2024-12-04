package com.wander_book.dto.request.room;

import com.wander_book.model.room.RoomState;
import com.wander_book.model.room.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimpleRoomDTO {
    private Long id;
    private String roomNumber;
    private RoomType roomType;
    private BigDecimal pricePerNight;
    private int maxOccupancy;
    private String description;
    private RoomState state;
    private String branchName;
}
