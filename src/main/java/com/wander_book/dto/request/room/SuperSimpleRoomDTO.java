package com.wander_book.dto.request.room;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuperSimpleRoomDTO {
    private Long id;
    private String roomNumber;
}
