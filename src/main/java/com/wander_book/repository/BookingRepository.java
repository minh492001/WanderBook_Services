package com.wander_book.repository;

import com.wander_book.model.branch.Branch;
import com.wander_book.model.booking.Booking;
import com.wander_book.model.booking.BookingStatus;
import com.wander_book.model.room.Room;
import com.wander_book.model.user.User;
import com.wander_book.repository.comon.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends BaseRepository<Booking> {

    Optional<Booking> findByConfirmationCode(String confirmationCode);

    @Transactional(readOnly = true)
    List<Booking> findByUser_Email(String email);

    @Transactional(readOnly = true)
    List<Booking> findByUser(User user);

    @Transactional(readOnly = true)
    List<Booking> findByStatus(BookingStatus status);

    @Transactional(readOnly = true)
    List<Booking> findByRoom(Room room);

    @Transactional(readOnly = true)
    List<Booking> findByRoom_Branch(Branch branch);

    // Find active bookings for a specific room during a time period
    @Transactional(readOnly = true)
    @Query("SELECT b FROM Booking b WHERE b.room = :room AND b.status = 'CONFIRMED' " +
            "AND b.checkInTimestamp < :end AND b.checkOutTimestamp > :start")
    List<Booking> findActiveBookingsForRoomDuringPeriod(
            @Param("room") Room room,
            @Param("start") Long start,
            @Param("end") Long end);

    // Find all confirmed bookings for a specific user
    @Transactional(readOnly = true)
    @Query("SELECT b FROM Booking b WHERE b.user = :user AND b.status = 'CONFIRMED'")
    List<Booking> findConfirmedBookingsByUser(@Param("user") User user);

    // Count bookings by room and status, useful for checking availability
    @Transactional(readOnly = true)
    long countByRoomAndStatus(Room room, BookingStatus status);

    // Custom query: Find bookings with overlapping periods (useful for room availability checks)
    @Transactional(readOnly = true)
    @Query("SELECT b FROM Booking b WHERE b.room = :room " +
            "AND b.status = 'CONFIRMED' " +
            "AND (:checkIn < b.checkOutTimestamp AND :checkOut > b.checkInTimestamp)")
    List<Booking> findOverlappingBookingsForRoom(
            @Param("room") Room room,
            @Param("checkIn") Long checkIn,
            @Param("checkOut") Long checkOut);

    // Custom query: Get all bookings within a branch for a specific time period
    @Transactional(readOnly = true)
    @Query("SELECT b FROM Booking b WHERE b.room.branch = :branch " +
            "AND b.checkInTimestamp >= :start AND b.checkOutTimestamp <= :end")
    List<Booking> findBookingsByBranchAndPeriod(
            @Param("branch") Branch branch,
            @Param("start") Long start,
            @Param("end") Long end);

    // Find all bookings with a specific status during a time period
    @Transactional(readOnly = true)
    @Query("SELECT b FROM Booking b WHERE b.status = :status " +
            "AND b.checkInTimestamp >= :start AND b.checkOutTimestamp <= :end")
    List<Booking> findBookingsByStatusAndPeriod(
            @Param("status") BookingStatus status,
            @Param("start") Long start,
            @Param("end") Long end);
}

