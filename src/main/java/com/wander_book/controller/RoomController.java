package com.wander_book.controller;

import com.wander_book.model.room.Room;
import com.wander_book.model.room.RoomState;
import com.wander_book.model.room.RoomType;
import com.wander_book.request.room.AddNewRoomRequest;
import com.wander_book.request.room.RoomUpdateRequest;
import com.wander_book.service.IRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v2/rooms")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
public class RoomController {

    private final IRoomService roomService;

    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("hello");
    }

    // Get all services
    @GetMapping("/all")
    public ResponseEntity<List<Room>> getAllServices() {
        List<Room> rooms = roomService.findAll();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long roomId) {
        return roomService.findByIdAndNotDeleted(roomId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<Room>> getRoomsByBranch(@PathVariable Long branchId) {
        return ResponseEntity.ok(roomService.findByBranchId(branchId));
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<Room>> getRoomsByState(@PathVariable RoomState state) {
        return ResponseEntity.ok(roomService.findByState(state));
    }

    @GetMapping("/type-price")
    public ResponseEntity<List<Room>> getRoomsByTypeAndPriceRange(
            @RequestParam RoomType roomType,
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice
    ) {
        return ResponseEntity.ok(roomService.findByRoomTypeAndPricePerNightBetween(roomType, minPrice, maxPrice));
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long roomId, @RequestBody RoomUpdateRequest roomUpdateRequest) {
        Room updatedRoom = roomService.updateRoom(roomId, roomUpdateRequest);
        return ResponseEntity.ok(updatedRoom);
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkRoomExistsByNumber(@RequestParam String roomNumber) {
        return ResponseEntity.ok(roomService.existsByRoomNumber(roomNumber));
    }

    @PostMapping("/add")
    public ResponseEntity<Room> addNewRoom(@RequestBody @Valid AddNewRoomRequest request) {
        Room room = roomService.addNewRoom(request);
        return ResponseEntity.ok(room);
    }
}
