package com.wander_book.controller;

import com.wander_book.model.booking.Booking;
import com.wander_book.model.booking.BookingStatus;
import com.wander_book.model.room.Room;
import com.wander_book.dto.request.SimpleBookingRequest;
import com.wander_book.service.IBookingService;
import com.wander_book.service.IRoomService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    // Get all bookings
//    @GetMapping("/all")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public ResponseEntity<List<Booking>> getAllUsers() {
//        List<Booking> bookings = bookingService.findAll();
//        return ResponseEntity.ok(bookings);
//    }

    // Create a new booking
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Booking> createBooking(@RequestBody SimpleBookingRequest bookingRequest) {
        Booking booking = bookingService.createBooking(bookingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }
    // Get a booking by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        Booking booking = bookingService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));
        return ResponseEntity.ok(booking);
    }

    // Update an existing booking
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody SimpleBookingRequest updateRequest) {
        Booking updatedBooking = bookingService.updateBooking(id, updateRequest);
        return ResponseEntity.ok(updatedBooking);
    }

    // Confirm a booking
    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<String> confirmBooking(@PathVariable Long id) {
        bookingService.confirmBooking(id);
        return ResponseEntity.ok("Booking confirmed successfully");
    }

    // Cancel a booking
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<String> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok("Booking canceled successfully");
    }

    // Extend a booking
    @PutMapping("/{id}/extend")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<String> extendBooking(@PathVariable Long id, @RequestParam Long newCheckOutTimestamp) {
        bookingService.extendBooking(id, newCheckOutTimestamp);
        return ResponseEntity.ok("Booking extended successfully");
    }

    // Delete a booking
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.softDeleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Get bookings by user email
    @GetMapping("/user")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<List<Booking>> getBookingsByUserEmail(@RequestParam String email) {
        List<Booking> bookings = bookingService.findByUserEmail(email);
        return ResponseEntity.ok(bookings);
    }

    // Get bookings by status
    @GetMapping("/status")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Booking>> getBookingsByStatus(@RequestParam BookingStatus status) {
        List<Booking> bookings = bookingService.findBookingsByStatus(status);
        return ResponseEntity.ok(bookings);
    }

    // Get active bookings for a room during a period
    @GetMapping("/room/{roomId}/active")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Booking>> getActiveBookingsForRoomDuringPeriod(
            @PathVariable Long roomId,
            @RequestParam Long start,
            @RequestParam Long end) {
        Room room = roomService.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + roomId));
        List<Booking> bookings = bookingService.findActiveBookingsForRoomDuringPeriod(room, start, end);
        return ResponseEntity.ok(bookings);
    }

}
