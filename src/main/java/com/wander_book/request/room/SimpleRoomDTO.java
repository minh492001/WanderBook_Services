package com.wander_book.request.room;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleRoomDTO {
    private Long id;
    private String roomNumber;
}
