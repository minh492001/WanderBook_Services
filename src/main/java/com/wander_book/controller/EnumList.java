package com.wander_book.controller;

import com.wander_book.model.booking.BookingStatus;
import com.wander_book.model.payment.PaymentMethod;
import com.wander_book.model.payment.PaymentStatus;
import com.wander_book.model.room.RoomState;
import com.wander_book.model.room.RoomType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v2/enums")
public class EnumList {

    @GetMapping("/rooms/states")
    public ResponseEntity<List<String>> getRoomStates() {
        List<String> roomStates = Arrays.stream(RoomState.values())
                .map(Enum::name) // Convert enum to string
                .toList();
        return ResponseEntity.ok(roomStates);
    }
    @GetMapping("/rooms/types")
    public ResponseEntity<List<String>> getRoomTypes() {
        List<String> roomStates = Arrays.stream(RoomType.values())
                .map(Enum::name) // Convert enum to string
                .toList();
        return ResponseEntity.ok(roomStates);
    }
    @GetMapping("/payments/status")
    public ResponseEntity<List<String>> getPaymentStatus() {
        List<String> roomStates = Arrays.stream(PaymentStatus.values())
                .map(Enum::name) // Convert enum to string
                .toList();
        return ResponseEntity.ok(roomStates);
    }
    @GetMapping("/payments/method")
    public ResponseEntity<List<String>> getPaymentMethod() {
        List<String> roomStates = Arrays.stream(PaymentMethod.values())
                .map(Enum::name) // Convert enum to string
                .toList();
        return ResponseEntity.ok(roomStates);
    }
    @GetMapping("/bookings/status")
    public ResponseEntity<List<String>> getBookingStatus() {
        List<String> roomStates = Arrays.stream(BookingStatus.values())
                .map(Enum::name) // Convert enum to string
                .toList();
        return ResponseEntity.ok(roomStates);
    }
}
