package com.wander_book.controller;

import com.wander_book.dto.request.booking.BookingRequestDTO;
import com.wander_book.dto.response.BookingResponseDTO;
import com.wander_book.model.booking.BookingStatus;
import com.wander_book.service.IBookingService;
import com.wander_book.service.IRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/bookings")
public class BookingController {
    private final IBookingService bookingService;
    private final IRoomService roomService;

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("hello");
    }

    // Create a new booking
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<BookingResponseDTO> createBooking(@Valid @RequestBody BookingRequestDTO requestDTO) {
        BookingResponseDTO responseDTO = bookingService.createBooking(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    // Update an existing booking
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BookingResponseDTO> updateBooking(
            @PathVariable Long id,
            @Valid @RequestBody BookingRequestDTO requestDTO) {
        BookingResponseDTO responseDTO = bookingService.updateBooking(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    // Delete a booking
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long bookingId) {
        bookingService.deleteBooking(bookingId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings() {
        List<BookingResponseDTO> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    // Get a booking by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Long id) {
        BookingResponseDTO booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(booking);
    }
    @GetMapping("/user/email/{email}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByUserEmail(@PathVariable String email) {
        List<BookingResponseDTO> bookings = bookingService.getBookingsByUserEmail(email);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByUserId(@PathVariable Long userId) {
        List<BookingResponseDTO> bookings = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByStatus(@PathVariable BookingStatus status) {
        List<BookingResponseDTO> bookings = bookingService.getBookingsByStatus(status);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/room/{roomId}/active")
    public ResponseEntity<List<BookingResponseDTO>> getActiveBookingsForRoom(
            @PathVariable Long roomId,
            @RequestParam Long start,
            @RequestParam Long end) {
        List<BookingResponseDTO> bookings = bookingService.getActiveBookingsForRoom(roomId, start, end);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/room/{roomId}/status/{status}/count")
    public ResponseEntity<Long> countBookingsByRoomAndStatus(
            @PathVariable Long roomId,
            @PathVariable BookingStatus status) {
        long count = bookingService.countBookingsByRoomAndStatus(roomId, status);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/{id}/extend")
    public ResponseEntity<BookingResponseDTO> extendBooking(
            @PathVariable Long id,
            @RequestParam Long newCheckOutTimestamp) {
        bookingService.extendBooking(id, newCheckOutTimestamp);
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

}
