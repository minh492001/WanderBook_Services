package com.wander_book.service.impl;

import com.wander_book.model.Branch;
import com.wander_book.model.room.Room;
import com.wander_book.model.room.RoomState;
import com.wander_book.model.room.RoomType;
import com.wander_book.repository.RoomRepository;
import com.wander_book.request.room.AddNewRoomRequest;
import com.wander_book.request.room.RoomUpdateRequest;
import com.wander_book.service.Common.BaseServiceImpl;
import com.wander_book.service.IBranchService;
import com.wander_book.service.IRoomService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.wander_book.service.Common.UpdateUtil.updateIfNotNull;

@Service
public class RoomService extends BaseServiceImpl<Room> implements IRoomService {

    private final RoomRepository roomRepository;
    private final IBranchService branchService;

    @Autowired
    public RoomService(RoomRepository roomRepository, IBranchService branchService) {
        this.repository = roomRepository;
        this.roomRepository = roomRepository;
        this.branchService = branchService;
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
    public List<Room> findByBranchId(Long branchId) {
        return roomRepository.findByBranchId(branchId);
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

}
