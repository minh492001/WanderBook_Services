package com.wander_book.repository;

import com.wander_book.model.room.Room;
import com.wander_book.model.room.RoomState;
import com.wander_book.model.room.RoomType;
import com.wander_book.repository.comon.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends BaseRepository<Room> {

    Optional<Room> findByRoomNumber(String roomNumber);
    
    @Query("SELECT r FROM Room r WHERE r.branch.id = :branchId")
    List<Room> findByBranchId(@Param("branchId") Long branchId);

    boolean existsByRoomNumber(String roomNumber);

    boolean existsByRoomNumberAndBranchId(String roomNumber, Long branchId);

    boolean existsByBranch_IdAndRoomNumber(Long branchId, String roomNumber);

    List<Room> findByState(RoomState state);

    @Query("SELECT r FROM Room r WHERE r.branch.id = :branchId AND r.state = :state")
    List<Room> findByBranchIdAndState(@Param("branchId") Long branchId, @Param("state") RoomState state);

    @Query("SELECT r FROM Room r WHERE r.roomType = :roomType AND r.pricePerNight BETWEEN :minPrice AND :maxPrice")
    List<Room> findByRoomTypeAndPriceRange(@Param("roomType") RoomType roomType, @Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);

    @Query("SELECT r FROM Room r WHERE r.branch.id = :branchId AND r.roomType = :roomType AND r.pricePerNight BETWEEN :minPrice AND :maxPrice")
    List<Room> findByBranchIdAndRoomTypeAndPriceRange(
            @Param("branchId") Long branchId,
            @Param("roomType") RoomType roomType,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice
    );
}
