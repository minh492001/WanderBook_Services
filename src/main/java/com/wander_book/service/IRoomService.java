package com.wander_book.service;

import com.wander_book.dto.request.room.RoomDetailsDTO;
import com.wander_book.dto.request.room.SimpleRoomDTO;
import com.wander_book.model.room.Room;
import com.wander_book.model.room.RoomState;
import com.wander_book.model.room.RoomType;
import com.wander_book.dto.request.room.AddNewRoomRequest;
import com.wander_book.dto.request.room.RoomUpdateRequest;
import com.wander_book.service.Common.IBaseService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IRoomService extends IBaseService<Room> {

    //No photo
    List<SimpleRoomDTO> getAllRooms();
    Optional<SimpleRoomDTO> findByRoomId(long id);
    Optional<SimpleRoomDTO> findByRoomNumber(String roomNumber);
    boolean existsByRoomNumber(String roomNumber);

    List<SimpleRoomDTO> findByState(RoomState state);

    List<SimpleRoomDTO> findByBranchIdAndState(Long branchId, RoomState state);
    List<SimpleRoomDTO> findByRoomTypeAndPriceRange(RoomType roomType, BigDecimal minPrice, BigDecimal maxPrice);
    List<SimpleRoomDTO> findByBranchIdAndRoomTypeAndPriceRange(Long branchId, RoomType roomType, BigDecimal minPrice, BigDecimal maxPrice);

    //With photo
    List<RoomDetailsDTO> getAllRoomsWithPhoto();
    Optional<RoomDetailsDTO> findByRoomNumberWithPhoto(String roomNumber);
    List<RoomDetailsDTO> getRoomsByBranchIdWithPhoto(Long branchId);

    List<RoomDetailsDTO> findByBranchIdAndStateWithPhoto(Long branchId, RoomState state);
    List<RoomDetailsDTO> findByRoomTypeAndPriceRangeWithPhoto(RoomType roomType, BigDecimal minPrice, BigDecimal maxPrice);
    List<RoomDetailsDTO> findByBranchIdAndRoomTypeAndPriceRangeWithPhoto(Long branchId, RoomType roomType, BigDecimal minPrice, BigDecimal maxPrice);

    RoomDetailsDTO addNewRoom(AddNewRoomRequest request);
    RoomDetailsDTO updateRoom(Long roomId, RoomUpdateRequest roomUpdateRequest);
    void deleteRoomById(Long id);

//    List<RoomResponse> getRoomsWithBookings();
//    List<RoomResponse> getRoomsWithBookingsByBranch(Long branchId);
}
