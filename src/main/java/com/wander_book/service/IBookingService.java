package com.wander_book.service;

import com.wander_book.model.booking.Booking;
import com.wander_book.model.booking.BookingStatus;
import com.wander_book.model.room.Room;
import com.wander_book.model.user.User;
import com.wander_book.request.SimpleBookingRequest;
import com.wander_book.service.Common.IBaseService;

import java.util.List;

public interface IBookingService extends IBaseService<Booking> {

    List<Booking> findByUser(User user);

    List<Booking> findByBranch(Long id);

    List<Booking> findByUserEmail(String email);

    Booking findByConfirmationCode(String confirmationCode);

    List<Booking> findBookingsByStatus(BookingStatus status);

    List<Booking> findBookingsByRoom(Room room);

    // Find active bookings for a specific room during a time period
    List<Booking> findActiveBookingsForRoomDuringPeriod(Room room, Long start, Long end);

    // Count bookings by room and status
    long countBookingsByRoomAndStatus(Room room, BookingStatus status);

    // Create a new booking
    Booking createBooking(SimpleBookingRequest bookingRequest);

    void confirmBooking(Long bookingId);

    void  cancelBooking(Long bookingId);

    Booking updateBooking(Long id, SimpleBookingRequest updateBooking);

    void extendBooking(Long bookingId, Long newCheckOutTimestamp);

    void softDeleteById(Long id);
}
