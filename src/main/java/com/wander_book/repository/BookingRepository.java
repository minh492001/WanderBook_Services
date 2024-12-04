package com.wander_book.repository;

import com.wander_book.model.branch.Branch;
import com.wander_book.model.booking.Booking;
import com.wander_book.model.booking.BookingStatus;
import com.wander_book.model.room.Room;
import com.wander_book.model.user.User;
import com.wander_book.repository.comon.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends BaseRepository<Booking> {

    Optional<Booking> findByConfirmationCode(String confirmationCode);

    List<Booking> findByUser_Email(String email);

    List<Booking> findByUser(User user);

    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findByRoom(Room room);

    List<Booking> findByRoom_Branch(Branch branch);

    // Find active bookings for a specific room during a time period
    @Query("SELECT b FROM Booking b WHERE b.room = :room AND b.status = 'CONFIRMED' " +
            "AND b.checkInTimestamp < :end AND b.checkOutTimestamp > :start")
    List<Booking> findActiveBookingsForRoomDuringPeriod(Room room, Long start, Long end);

    // Find all confirmed bookings for a specific user
    @Query("SELECT b FROM Booking b WHERE b.user = :user AND b.status = 'CONFIRMED'")
    List<Booking> findConfirmedBookingsByUser(User user);

    // Count bookings by room and status, useful for checking availability
    long countByRoomAndStatus(Room room, BookingStatus status);
}
