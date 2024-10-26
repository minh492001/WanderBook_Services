package com.wander_book.response;

import com.wander_book.model.enums.RoomType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class RoomResponse {
    private Long id;
    private RoomType roomType;
    private BigDecimal roomPrice;
    private boolean isBooked;
    private String photo;

    public RoomResponse(Long id, RoomType roomType, BigDecimal roomPrice) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
    }

    public RoomResponse(Long id, RoomType roomType, BigDecimal roomPrice, boolean isBooked, byte[] photoBBytes) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.isBooked = isBooked;
        this.photo = photoBBytes != null ? Base64.encodeBase64String(photoBBytes) : null;
    }
}
