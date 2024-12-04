package com.wander_book.controller;

import com.wander_book.dto.request.room.RoomDetailsDTO;
import com.wander_book.dto.request.room.SimpleRoomDTO;
import com.wander_book.model.room.Room;
import com.wander_book.model.room.RoomState;
import com.wander_book.model.room.RoomType;
import com.wander_book.dto.request.room.AddNewRoomRequest;
import com.wander_book.dto.request.room.RoomUpdateRequest;
import com.wander_book.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v2/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final IRoomService roomService;

    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("hello");
    }

    // Without Photo and Description
    @GetMapping("/all")
    public ResponseEntity<List<SimpleRoomDTO>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SimpleRoomDTO> getRoomById(@PathVariable long id) {
        Optional<SimpleRoomDTO> room = roomService.findByRoomId(id);
        return room.map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + id));
    }

    @GetMapping("/number/{roomNumber}")
    public ResponseEntity<SimpleRoomDTO> getRoomByRoomNumber(@PathVariable String roomNumber) {
        Optional<SimpleRoomDTO> room = roomService.findByRoomNumber(roomNumber);
        return room.map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with room number: " + roomNumber));
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<SimpleRoomDTO>> getRoomsByState(@PathVariable RoomState state) {
        return ResponseEntity.ok(roomService.findByState(state));
    }

    @GetMapping("/branch/{branchId}/state/{state}")
    public ResponseEntity<List<SimpleRoomDTO>> getRoomsByBranchIdAndState(
            @PathVariable Long branchId,
            @PathVariable RoomState state) {
        List<SimpleRoomDTO> rooms = roomService.findByBranchIdAndState(branchId, state);
        if (rooms.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/filter/findByRoomTypeAndPriceRange")
    public ResponseEntity<List<SimpleRoomDTO>> findByRoomTypeAndPriceRange(
            @RequestParam RoomType roomType,
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<SimpleRoomDTO> rooms = roomService.findByRoomTypeAndPriceRange(roomType, minPrice, maxPrice);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/filter/branch/{branchId}/findByBranchIdAndRoomTypeAndPriceRange")
    public ResponseEntity<List<SimpleRoomDTO>> findByBranchIdAndRoomTypeAndPriceRange(
            @PathVariable Long branchId,
            @RequestParam RoomType roomType,
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<SimpleRoomDTO> rooms = roomService.findByBranchIdAndRoomTypeAndPriceRange(branchId, roomType, minPrice, maxPrice);
        return ResponseEntity.ok(rooms);
    }

    //With Photo and Description
    @GetMapping("/detail/all")
    public ResponseEntity<List<RoomDetailsDTO>> getAllRoomsWithPhoto() {
        List<RoomDetailsDTO> rooms = roomService.getAllRoomsWithPhoto();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/detail/number/{roomNumber}")
    public ResponseEntity<RoomDetailsDTO> findByRoomNumberWithPhoto(@PathVariable String roomNumber) {
        Optional<RoomDetailsDTO> room = roomService.findByRoomNumberWithPhoto(roomNumber);
        return room.map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with room number: " + roomNumber));
    }

    @GetMapping("/{branchId}/rooms-with-photo")
    public ResponseEntity<List<RoomDetailsDTO>> getRoomsByBranchIdWithPhoto(@PathVariable Long branchId) {
        List<RoomDetailsDTO> rooms = roomService.getRoomsByBranchIdWithPhoto(branchId);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/detail/branch/{branchId}/state/{state}")
    public ResponseEntity<List<RoomDetailsDTO>> findByBranchIdAndStateWithPhoto(
            @PathVariable Long branchId,
            @PathVariable RoomState state) {
        List<RoomDetailsDTO> rooms = roomService.findByBranchIdAndStateWithPhoto(branchId, state);
        if (rooms.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/detail/filter/findByRoomTypeAndPriceRange")
    public ResponseEntity<List<RoomDetailsDTO>> findByRoomTypeAndPriceRangeWithPhoto(
            @RequestParam RoomType roomType,
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<RoomDetailsDTO> rooms = roomService.findByRoomTypeAndPriceRangeWithPhoto(roomType, minPrice, maxPrice);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/detail/filter/branch/{branchId}/findByBranchIdAndRoomTypeAndPriceRange")
    public ResponseEntity<List<RoomDetailsDTO>> findByBranchIdAndRoomTypeAndPriceRangeWithPhoto(
            @PathVariable Long branchId,
            @RequestParam RoomType roomType,
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<RoomDetailsDTO> rooms = roomService.findByBranchIdAndRoomTypeAndPriceRangeWithPhoto(branchId, roomType, minPrice, maxPrice);
        return ResponseEntity.ok(rooms);
    }

    @PostMapping("/add")
    public ResponseEntity<RoomDetailsDTO> addNewRoom(@RequestBody AddNewRoomRequest request) {
        RoomDetailsDTO newRoom = roomService.addNewRoom(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRoom);
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<RoomDetailsDTO> updateRoom(
            @PathVariable Long roomId,
            @RequestBody RoomUpdateRequest roomUpdateRequest) {
        RoomDetailsDTO updatedRoom = roomService.updateRoom(roomId, roomUpdateRequest);
        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoomById(id);
        return ResponseEntity.ok("Room soft-deleted successfully.");
    }

//    @GetMapping("/all")
//    public ResponseEntity<List<RoomResponse>> getAllRoomsWithFutureBookings() {
//        List<RoomResponse> rooms = roomService.getRoomsWithBookings();
//        return ResponseEntity.ok(rooms);
//    }
//
//    @GetMapping("/branch/{branchId}")
//    public ResponseEntity<List<RoomResponse>> getRoomsWithBookingsByBranch(@PathVariable Long branchId) {
//        List<RoomResponse> rooms = roomService.getRoomsWithBookingsByBranch(branchId);
//        return ResponseEntity.ok(rooms);
//    }
}
