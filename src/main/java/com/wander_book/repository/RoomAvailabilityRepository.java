package com.wander_book.repository;

import com.wander_book.model.booking.Booking;
import com.wander_book.model.room.Room;
import com.wander_book.model.room.RoomAvailability;
import com.wander_book.repository.comon.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomAvailabilityRepository extends BaseRepository<RoomAvailability> {
    @Query("SELECT ra FROM RoomAvailability ra WHERE ra.room = :room AND ra.endDate >= :currentTimestamp")
    List<RoomAvailability> findFutureBookingsByRoom(@Param("room") Room room, @Param("currentTimestamp") Long currentTimestamp);

    @Query("SELECT ra FROM RoomAvailability ra WHERE ra.room IN :rooms AND ra.endDate >= :currentDate")
    List<RoomAvailability> findFutureBookingsByRooms(@Param("rooms") List<Room> rooms, @Param("currentDate") Long currentDate);

    @Query("SELECT ra FROM RoomAvailability ra WHERE ra.room = :room AND ra.endDate > :startDate AND ra.startDate < :endDate")
    List<RoomAvailability> findConflictingBookings(@Param("room") Room room,
                                                   @Param("startDate") Long startDate,
                                                   @Param("endDate") Long endDate);

    void deleteByBooking(Booking booking);

    Optional<RoomAvailability> findByBooking(Booking booking);
}
