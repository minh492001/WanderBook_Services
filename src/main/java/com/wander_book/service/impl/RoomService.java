package com.wander_book.service.impl;

import com.wander_book.dto.request.room.AddNewRoomRequest;
import com.wander_book.dto.request.room.RoomDetailsDTO;
import com.wander_book.dto.request.room.SimpleRoomDTO;
import com.wander_book.mapper.RoomMapper;
import com.wander_book.model.branch.Branch;
import com.wander_book.model.room.Room;
import com.wander_book.model.room.RoomAvailability;
import com.wander_book.model.room.RoomState;
import com.wander_book.model.room.RoomType;
import com.wander_book.repository.RoomAvailabilityRepository;
import com.wander_book.repository.RoomRepository;
import com.wander_book.dto.request.room.RoomUpdateRequest;
import com.wander_book.service.Common.BaseServiceImpl;
import com.wander_book.service.Common.Utility;
import com.wander_book.service.IBranchService;
import com.wander_book.service.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService extends BaseServiceImpl<Room> implements IRoomService {

    private final RoomRepository roomRepository;
    private final IBranchService branchService;
    private final RoomAvailabilityRepository roomAvailabilityRepository;
    private final RoomMapper roomMapper;

    @Autowired
    public RoomService(RoomRepository roomRepository, IBranchService branchService, RoomAvailabilityRepository roomAvailabilityRepository, RoomMapper roomMapper) {
        this.repository = roomRepository;
        this.roomRepository = roomRepository;
        this.branchService = branchService;
        this.roomAvailabilityRepository = roomAvailabilityRepository;
        this.roomMapper = roomMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SimpleRoomDTO> getAllRooms() {
        List<Room> rooms = attachRoomAvailabilities(roomRepository.findAll());
        return rooms.stream().map(roomMapper::toSimpleRoomDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SimpleRoomDTO> findByRoomId(long id) {
        return roomRepository.findById(id)
                .map(this::attachRoomAvailabilities)
                .map(roomMapper::toSimpleRoomDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SimpleRoomDTO> findByRoomNumber(String roomNumber) {
        return roomRepository.findByRoomNumber(roomNumber)
                .map(this::attachRoomAvailabilities)
                .map(roomMapper::toSimpleRoomDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByRoomNumber(String roomNumber) {
        return roomRepository.existsByRoomNumber(roomNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SimpleRoomDTO> findByState(RoomState state) {
        List<Room> rooms = attachRoomAvailabilities(roomRepository.findByState(state));
        return rooms.stream().map(roomMapper::toSimpleRoomDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SimpleRoomDTO> findByBranchIdAndState(Long branchId, RoomState state) {
        List<Room> rooms = attachRoomAvailabilities(roomRepository.findByBranchIdAndState(branchId, state));
        return rooms.stream().map(roomMapper::toSimpleRoomDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SimpleRoomDTO> findByRoomTypeAndPriceRange(RoomType roomType, BigDecimal minPrice, BigDecimal maxPrice) {
        List<Room> rooms = attachRoomAvailabilities(roomRepository.findByRoomTypeAndPriceRange(roomType, minPrice, maxPrice));
        return rooms.stream().map(roomMapper::toSimpleRoomDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SimpleRoomDTO> findByBranchIdAndRoomTypeAndPriceRange(Long branchId, RoomType roomType, BigDecimal minPrice, BigDecimal maxPrice) {
        List<Room> rooms = attachRoomAvailabilities(roomRepository.findByBranchIdAndRoomTypeAndPriceRange(branchId, roomType, minPrice, maxPrice));
        return rooms.stream().map(roomMapper::toSimpleRoomDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDetailsDTO> getAllRoomsWithPhoto() {
        List<Room> rooms = attachRoomAvailabilities(roomRepository.findAll());
        return rooms.stream().map(roomMapper::toRoomDetailsDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoomDetailsDTO> findByRoomNumberWithPhoto(String roomNumber) {
        return roomRepository.findByRoomNumber(roomNumber)
                .map(this::attachRoomAvailabilities)
                .map(roomMapper::toRoomDetailsDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDetailsDTO> getRoomsByBranchIdWithPhoto(Long branchId) {
        List<Room> rooms = attachRoomAvailabilities(roomRepository.findByBranchId(branchId));
        return rooms.stream().map(roomMapper::toRoomDetailsDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDetailsDTO> findByBranchIdAndStateWithPhoto(Long branchId, RoomState state) {
        List<Room> rooms = attachRoomAvailabilities(roomRepository.findByBranchIdAndState(branchId, state));
        return rooms.stream().map(roomMapper::toRoomDetailsDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDetailsDTO> findByRoomTypeAndPriceRangeWithPhoto(RoomType roomType, BigDecimal minPrice, BigDecimal maxPrice) {
        List<Room> rooms = attachRoomAvailabilities(roomRepository.findByRoomTypeAndPriceRange(roomType, minPrice, maxPrice));
        return rooms.stream().map(roomMapper::toRoomDetailsDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDetailsDTO> findByBranchIdAndRoomTypeAndPriceRangeWithPhoto(Long branchId, RoomType roomType, BigDecimal minPrice, BigDecimal maxPrice) {
        List<Room> rooms = attachRoomAvailabilities(roomRepository.findByBranchIdAndRoomTypeAndPriceRange(branchId, roomType, minPrice, maxPrice));
        return rooms.stream().map(roomMapper::toRoomDetailsDTO).toList();
    }

    @Override
    public RoomDetailsDTO addNewRoom(AddNewRoomRequest request) {
        Branch branch = branchService.findById(request.getBranchId())
                .orElseThrow(() -> new IllegalArgumentException("Branch not found with ID: " + request.getBranchId()));
        boolean exists = roomRepository.existsByRoomNumberAndBranchId(request.getRoomNumber(), request.getBranchId());
        if (exists) {
            throw new IllegalArgumentException("Room with number " + request.getRoomNumber() + " already exists in the branch with ID: " + request.getBranchId());
        }

        Room room = roomMapper.toRoom(request, branch);
        Room savedRoom = roomRepository.save(room);

        return roomMapper.toRoomDetailsDTO(savedRoom);
    }

   @Override
    public RoomDetailsDTO updateRoom(Long roomId, RoomUpdateRequest roomUpdateRequest) {
        // Find the existing room
        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with ID: " + roomId));

        // Check for duplicate room number in the same branch
        if (roomUpdateRequest.getRoomNumber() != null &&
                !roomUpdateRequest.getRoomNumber().equals(existingRoom.getRoomNumber())) {
            boolean roomNumberExists = roomRepository.existsByBranch_IdAndRoomNumber(
                    existingRoom.getBranch().getId(), roomUpdateRequest.getRoomNumber());
            if (roomNumberExists) {
                throw new IllegalArgumentException("Room number already exists in this branch");
            }
        }

        // Update the fields using the Utility class
        Utility.updateIfNotNull(roomUpdateRequest.getRoomNumber(), existingRoom::setRoomNumber);
        Utility.updateIfNotNull(roomUpdateRequest.getRoomType(), existingRoom::setRoomType);
        Utility.updateIfNotNull(roomUpdateRequest.getPricePerNight(), existingRoom::setPricePerNight);
        Utility.updateIfNotNull(roomUpdateRequest.getMaxOccupancy(), existingRoom::setMaxOccupancy);
        Utility.updateIfNotNull(roomUpdateRequest.getDescription(), existingRoom::setDescription);

        // Decode and update photo if provided
        if (roomUpdateRequest.getPhoto() != null) {
            existingRoom.setPhoto(Base64.getDecoder().decode(roomUpdateRequest.getPhoto()));
        }

        // Save the updated room
        Room updatedRoom = roomRepository.save(existingRoom);

        // Convert to DTO and return
        return roomMapper.toRoomDetailsDTO(updatedRoom);
    }

    @Override
    public void deleteRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with ID: " + id));
        roomRepository.softDelete(room);
    }

    // Methods for description and photo
    public String getRoomDescription(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + roomId));
        return room.getDescription();
    }

    public byte[] getRoomPhoto(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + roomId));
        return room.getPhoto();
    }

//    public List<RoomResponse> getRoomsWithBookings() {
//        List<Room> rooms = roomRepository.findAll();
//        Long currentTimestamp = System.currentTimeMillis();
//
//        // Load future bookings for all rooms
//        rooms.forEach(room -> {
//            List<RoomAvailability> futureBookings = roomAvailabilityRepository.findFutureBookingsByRoom(room, currentTimestamp);
//            room.setFutureBookings(futureBookings);
//        });
//
//        return rooms.stream().map(roomMapper::toDto).collect(Collectors.toList());
//    }
//
//    public List<RoomResponse> getRoomsWithBookingsByBranch(Long branchId) {
//        Branch branch = branchService.findById(branchId)
//                .orElseThrow(() -> new IllegalArgumentException("Branch not found with ID: " + branchId));
//        List<Room> rooms = roomRepository.findByBranch(branch);
//        Long currentTimestamp = System.currentTimeMillis();
//
//        // Load future bookings for all rooms in the branch
//        rooms.forEach(room -> {
//            List<RoomAvailability> futureBookings = roomAvailabilityRepository.findFutureBookingsByRoom(room, currentTimestamp);
//            room.setFutureBookings(futureBookings);
//        });
//
//        return rooms.stream().map(roomMapper::toDto).collect(Collectors.toList());
//    }
private List<Room> attachRoomAvailabilities(List<Room> rooms) {
    Long currentTimestamp = System.currentTimeMillis();

    rooms.forEach(room -> {
        List<RoomAvailability> futureBookings = roomAvailabilityRepository.findFutureBookingsByRoom(room, currentTimestamp);
        room.setFutureBookings(futureBookings);
    });

    return rooms;
}

    private Room attachRoomAvailabilities(Room room) {
        Long currentTimestamp = System.currentTimeMillis();
        List<RoomAvailability> futureBookings = roomAvailabilityRepository.findFutureBookingsByRoom(room, currentTimestamp);
        room.setFutureBookings(futureBookings);
        return room;
    }
}
