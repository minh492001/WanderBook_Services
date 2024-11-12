package com.wander_book.service.impl;

import com.wander_book.model.Branch;
import com.wander_book.model.booking.Booking;
import com.wander_book.model.booking.BookingStatus;
import com.wander_book.model.room.Room;
import com.wander_book.model.user.User;
import com.wander_book.repository.BookingRepository;
import com.wander_book.request.SimpleBookingRequest;
import com.wander_book.service.Common.BaseServiceImpl;
import com.wander_book.service.IBookingService;
import com.wander_book.service.IBranchService;
import com.wander_book.service.IRoomService;
import com.wander_book.service.IUserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.wander_book.service.Common.UpdateUtil.*;

@Service
public class BookingService extends BaseServiceImpl<Booking> implements IBookingService {

    private final BookingRepository bookingRepository;
    private final IBranchService branchService;
    private final IRoomService roomService;
    private final IUserService userService;


    public BookingService(BookingRepository bookingRepository, BranchService branchService, RoomService roomService, IUserService userService) {
        this.roomService = roomService;
        this.userService = userService;
        this.repository = bookingRepository;
        this.bookingRepository = bookingRepository;
        this.branchService = branchService;
    }

    @Override
    public List<Booking> findByUser(User user) {
        return bookingRepository.findByUser(user);
    }

    @Override
    public List<Booking> findByBranch(Long id) {
        Optional<Branch> branchOptional = branchService.findById(id);

        if (branchOptional.isPresent()) {
            Branch branch = branchOptional.get();
            return bookingRepository.findByRoom_Branch(branch);
        }

        // Return an empty list if branch is not found, or you may throw an exception if preferred
        return List.of();
    }

    @Override
    public List<Booking> findByUserEmail(String email) {
        return bookingRepository.findByUser_Email(email);
    }

    @Override
    public Booking findByConfirmationCode(String confirmationCode) {
        return bookingRepository.findByConfirmationCode(confirmationCode).orElse(null);
    }

    @Override
    public List<Booking> findBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }

    @Override
    public List<Booking> findBookingsByRoom(Room room) {
        return bookingRepository.findByRoom(room);
    }

    @Override
    public List<Booking> findActiveBookingsForRoomDuringPeriod(Room room, Long start, Long end) {
        if (end - start < 86400000L) {
            throw new IllegalArgumentException("Start date must be at least 1 day before end date.");
        }

        return bookingRepository.findActiveBookingsForRoomDuringPeriod(room, start, end);
    }

    @Override
    public long countBookingsByRoomAndStatus(Room room, BookingStatus status) {
        return bookingRepository.countByRoomAndStatus(room, status);
    }

    @Override
    public Booking createBooking(SimpleBookingRequest bookingRequest) {
        if (bookingRequest.getCheckOutTimestamp() - bookingRequest.getCheckInTimestamp() < 86400000L) {
            throw new IllegalArgumentException("Check-in date must be at least 1 day before check-out date.");
        }

        Room room = roomService.findById(bookingRequest.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + bookingRequest.getRoomId()));

        if ((bookingRequest.getAdultsCount()+bookingRequest.getChildrenCount()) > room.getMaxOccupancy()) {
            throw new IllegalArgumentException("Total guests cannot exceed room's max occupancy of " + room.getMaxOccupancy());
        }

        User user = userService.findById(bookingRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + bookingRequest.getUserId()));

        Booking booking = Booking.builder()
                .user(user)
                .room(room)
                .checkInTimestamp(bookingRequest.getCheckInTimestamp())
                .checkOutTimestamp(bookingRequest.getCheckOutTimestamp())
                .adultsCount(bookingRequest.getAdultsCount())
                .childrenCount(bookingRequest.getChildrenCount())
                .totalGuests(bookingRequest.getAdultsCount() + bookingRequest.getChildrenCount())
                .notes(bookingRequest.getNotes())
                .status(bookingRequest.getStatus() != null ? bookingRequest.getStatus() : BookingStatus.PENDING) // Set default status to PENDING if null
                .build();

        booking.initializeBooking();
        return bookingRepository.save(booking);
    }

    @Override
    public void confirmBooking(Long bookingId) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            if (!(booking.getStatus() == BookingStatus.PENDING)) {
                throw new IllegalStateException("Booking is not in the state to be confirmed");
            }
            booking.setStatus(BookingStatus.CONFIRMED);
            booking.getRoom().bookRoom();
            bookingRepository.save(booking);
        }
        throw new EntityNotFoundException("Booking not found with id: " + bookingId);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            if (booking.getStatus() == BookingStatus.PENDING || booking.getStatus() == BookingStatus.CONFIRMED) {
                booking.setStatus(BookingStatus.CANCELED);
                booking.getRoom().cancelBooking();
                bookingRepository.save(booking);
                return;
            }
            throw new IllegalStateException("Booking is already canceled or completed");
        }
        throw new EntityNotFoundException("Booking not found with id: " + bookingId);
    }

    @Override
    public Booking updateBooking(Long id, SimpleBookingRequest updateBooking) {

        if (updateBooking.getCheckOutTimestamp() - updateBooking.getCheckInTimestamp() < 86400000L) {
            throw new IllegalArgumentException("Check-in date must be at least 1 day before check-out date.");
        }

        Room room = roomService.findById(updateBooking.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + updateBooking.getRoomId()));

        if (updateBooking.getTotalGuests() > room.getMaxOccupancy()) {
            throw new IllegalArgumentException("Total guests cannot exceed room's max occupancy of " + room.getMaxOccupancy());
        }

        User user = userService.findById(updateBooking.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + updateBooking.getUserId()));

        Optional<Booking> bookingOptional = bookingRepository.findById(id);

        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();

            booking.setRoom(room);
            booking.setUser(user);

            // Update booking details based on editBookingRequest properties
            updateIfNotNull(updateBooking.getCheckInTimestamp(), booking::setCheckInTimestamp);
            updateIfNotNull(updateBooking.getCheckOutTimestamp(), booking::setCheckOutTimestamp);
            updateIfNotNull(updateBooking.getNotes(), booking::setNotes);
            updateIfNotNull(updateBooking.getStatus(), booking::setStatus);

            updateIfPositive(updateBooking.getAdultsCount(), booking::setAdultsCount);
            updateIfNonNegative(updateBooking.getChildrenCount(), booking::setChildrenCount);
            updateIfPositive(updateBooking.getTotalGuests(), booking::setTotalGuests);

            return bookingRepository.save(booking);
        }
        throw new EntityNotFoundException("Booking not found with id: " + id);
    }

    @Override
    public void extendBooking(Long id, Long newCheckOutTimestamp) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            if (booking.getStatus() == BookingStatus.COMPLETED || booking.getStatus() == BookingStatus.CANCELED) {
                booking.setCheckOutTimestamp(newCheckOutTimestamp);
                bookingRepository.save(booking);
            }
            throw new IllegalStateException("Booking cannot be extended");
        }
        throw new EntityNotFoundException("Booking not found with id: " + id);
    }

    @Override
    public void softDeleteById(Long id) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            booking.onDelete();
            bookingRepository.save(booking);
        }
        throw new EntityNotFoundException("Booking not found with id: " + id);
    }

}
