package com.wander_book.service.impl;

import com.wander_book.mapper.RoomMapper;
import com.wander_book.model.Branch;
import com.wander_book.model.room.Room;
import com.wander_book.model.room.RoomAvailability;
import com.wander_book.model.room.RoomState;
import com.wander_book.model.room.RoomType;
import com.wander_book.repository.BranchRepository;
import com.wander_book.repository.RoomAvailabilityRepository;
import com.wander_book.repository.RoomRepository;
import com.wander_book.request.room.AddNewRoomRequest;
import com.wander_book.request.room.RoomUpdateRequest;
import com.wander_book.response.RoomAvailabilityResponse;
import com.wander_book.response.RoomResponse;
import com.wander_book.service.Common.BaseServiceImpl;
import com.wander_book.service.IBranchService;
import com.wander_book.service.IRoomService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.wander_book.service.Common.Utility.updateIfNotNull;

@Service
public class RoomService extends BaseServiceImpl<Room> implements IRoomService {

    private final RoomRepository roomRepository;
    private final IBranchService branchService;
    private final RoomAvailabilityRepository roomAvailabilityRepository;
    private final BranchRepository branchRepository;
    private final RoomMapper roomMapper;

    @Autowired
    public RoomService(RoomRepository roomRepository, IBranchService branchService, RoomAvailabilityRepository roomAvailabilityRepository, BranchRepository branchRepository, RoomMapper roomMapper) {
        this.repository = roomRepository;
        this.roomRepository = roomRepository;
        this.branchService = branchService;
        this.roomAvailabilityRepository = roomAvailabilityRepository;
        this.branchRepository = branchRepository;
        this.roomMapper = roomMapper;
    }

    @Override
    public Optional<Room> findByRoomNumber(String roomNumber) {
        return roomRepository.findByRoomNumber(roomNumber);
    }

    @Override
    public List<Room> findByState(RoomState state) {
        return roomRepository.findByState(state);
    }

    @Override
    public List<Room> findByBranch(Long branchId) {
        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new EntityNotFoundException("Branch not found with id: " + branchId));
        return roomRepository.findByBranch(branch);
    }

    @Override
    public List<Room> findByBranchIdAndState(Long branchId, RoomState state) {
        return roomRepository.findByBranch_IdAndState(branchId, state);
    }

    @Override
    public List<Room> findByRoomTypeAndPricePerNightBetween(RoomType roomType, BigDecimal minPrice, BigDecimal maxPrice) {
        return roomRepository.findByRoomTypeAndPricePerNightBetween(roomType, minPrice, maxPrice);
    }

    @Override
    public List<Room> findByBranchIdAndRoomTypeAndPricePerNightBetween(Long branchId, RoomType roomType, BigDecimal minPrice, BigDecimal maxPrice) {
        return roomRepository.findByBranch_IdAndRoomTypeAndPricePerNightBetween(branchId, roomType, minPrice, maxPrice);
    }

    @Override
    public boolean existsByRoomNumber(String roomNumber) {
        return roomRepository.existsByRoomNumber(roomNumber);
    }

    public Room addNewRoom(AddNewRoomRequest request) {
        Branch branch = branchService.findById(request.getBranchId())
                .orElseThrow(() -> new IllegalArgumentException("Branch not found with ID: " + request.getBranchId()));

        Room room = new Room(branch,
                request.getRoomNumber(),
                request.getRoomType(),
                request.getPricePerNight(),
                RoomState.OPEN,
                request.getMaxOccupancy());

        // Only set description and photo if they are not null
        if (request.getDescription() != null) {
            room.setDescription(request.getDescription());
        }
        if (request.getPhoto() != null) {
            room.setPhoto(request.getPhoto());
        }

        return roomRepository.save(room);
    }

    @Override
    @Transactional
    public Room updateRoom(Long roomId, RoomUpdateRequest roomUpdateRequest) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with ID: " + roomId));

        // Update only non-null fields from the request
        updateIfNotNull(roomUpdateRequest.getRoomNumber(), room::setRoomNumber);
        updateIfNotNull(roomUpdateRequest.getRoomType(), room::setRoomType);
        updateIfNotNull(roomUpdateRequest.getPricePerNight(), room::setPricePerNight);
        updateIfNotNull(roomUpdateRequest.getMaxOccupancy(), room::setMaxOccupancy);
        updateIfNotNull(roomUpdateRequest.getDescription(), room::setDescription);
        updateIfNotNull(roomUpdateRequest.getState(), room::setState);
        updateIfNotNull(roomUpdateRequest.getPhoto(), room::setPhoto);

        return roomRepository.save(room);
    }

    @Override
    public List<RoomResponse> getRoomsWithBookings() {
        List<Room> rooms = roomRepository.findAll();
        Long currentTimestamp = System.currentTimeMillis();

        // Load future bookings for all rooms
        rooms.forEach(room -> {
            List<RoomAvailability> futureBookings = roomAvailabilityRepository.findFutureBookingsByRoom(room, currentTimestamp);
            room.setFutureBookings(futureBookings);
        });

        return rooms.stream().map(roomMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<RoomResponse> getRoomsWithBookingsByBranch(Long branchId) {
        Branch branch = branchService.findById(branchId)
                .orElseThrow(() -> new IllegalArgumentException("Branch not found with ID: " + branchId));
        List<Room> rooms = roomRepository.findByBranch(branch);
        Long currentTimestamp = System.currentTimeMillis();

        // Load future bookings for all rooms in the branch
        rooms.forEach(room -> {
            List<RoomAvailability> futureBookings = roomAvailabilityRepository.findFutureBookingsByRoom(room, currentTimestamp);
            room.setFutureBookings(futureBookings);
        });

        return rooms.stream().map(roomMapper::toDto).collect(Collectors.toList());
    }
}
