package com.wander_book.repository;

import com.wander_book.model.Branch;
import com.wander_book.model.room.Room;
import com.wander_book.model.room.RoomState;
import com.wander_book.model.room.RoomType;
import com.wander_book.repository.comon.BaseRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends BaseRepository<Room> {

    // Find room by room number
    Optional<Room> findByRoomNumber(String roomNumber);

    // Find all rooms by state (e.g., available, booked, etc.)
    List<Room> findByState(RoomState state);

    // Find all rooms by branch id
    List<Room> findByBranch(Branch branch);

    // Find all rooms by branch id and state
    List<Room> findByBranch_IdAndState(Long branchId, RoomState state);

    // Find rooms by room type and price range
    List<Room> findByRoomTypeAndPricePerNightBetween(RoomType roomType, BigDecimal minPrice, BigDecimal maxPrice);

    // Find all rooms by branch and room type and within a price range
    List<Room> findByBranch_IdAndRoomTypeAndPricePerNightBetween(Long branchId, RoomType roomType, BigDecimal minPrice, BigDecimal maxPrice);

    // Check if a room exists by room number
    boolean existsByRoomNumber(String roomNumber);

//    @Query("SELECT COUNT(r) FROM Room r WHERE r.branch = :branch")
//    Long countByBranch(@Param("branch") Branch branch);
//
//    @Query("SELECT r.roomType, COUNT(r) FROM Room r WHERE r.branch = :branch GROUP BY r.roomType")
//    List<Map<String, Object>> countRoomsByTypeInBranch(@Param("branch") Branch branch);
//
//    @Query("SELECT r.roomType, COUNT(r) FROM Room r GROUP BY r.roomType")
//    List<Map<String, Object>> countRoomsByType();

}
