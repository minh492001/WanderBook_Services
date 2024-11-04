package com.wander_book.service;

import com.wander_book.model.room.Room;
import com.wander_book.model.room.RoomState;
import com.wander_book.model.room.RoomType;
import com.wander_book.request.room.AddNewRoomRequest;
import com.wander_book.request.room.RoomUpdateRequest;
import com.wander_book.service.Common.IBaseService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IRoomService extends IBaseService<Room> {

    Optional<Room> findByRoomNumber(String roomNumber);

    List<Room> findByState(RoomState state);

    List<Room> findByBranchId(Long branchId);

    List<Room> findByBranchIdAndState(Long branchId, RoomState state);

    List<Room> findByRoomTypeAndPricePerNightBetween(RoomType roomType, BigDecimal minPrice, BigDecimal maxPrice);

    List<Room> findByBranchIdAndRoomTypeAndPricePerNightBetween(Long branchId, RoomType roomType, BigDecimal minPrice, BigDecimal maxPrice);

    boolean existsByRoomNumber(String roomNumber);

    public Room addNewRoom(AddNewRoomRequest request);

    public Room updateRoom(Long roomId, RoomUpdateRequest roomUpdateRequest);
}
