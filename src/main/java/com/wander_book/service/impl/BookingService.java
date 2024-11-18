package com.wander_book.service.impl;

import com.wander_book.model.Branch;
import com.wander_book.model.booking.Booking;
import com.wander_book.model.booking.BookingStatus;
import com.wander_book.model.room.Room;
import com.wander_book.model.room.RoomAvailability;
import com.wander_book.model.user.User;
import com.wander_book.repository.BookingRepository;
import com.wander_book.repository.RoomAvailabilityRepository;
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

import static com.wander_book.service.Common.Utility.*;

@Service
public class BookingService extends BaseServiceImpl<Booking> implements IBookingService {

    private final BookingRepository bookingRepository;
    private final IBranchService branchService;
    private final IRoomService roomService;
    private final IUserService userService;
    private final RoomAvailabilityRepository roomAvailabilityRepository;


    public BookingService(BookingRepository bookingRepository, BranchService branchService, RoomService roomService, IUserService userService, RoomAvailabilityRepository roomAvailabilityRepository) {
        this.roomService = roomService;
        this.userService = userService;
        this.roomAvailabilityRepository = roomAvailabilityRepository;
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

        if ((bookingRequest.getAdultsCount() + bookingRequest.getChildrenCount()) > room.getMaxOccupancy()) {
            throw new IllegalArgumentException("Total guests cannot exceed room's max occupancy of " + room.getMaxOccupancy());
        }

        List<RoomAvailability> conflictingBookings = roomAvailabilityRepository.findConflictingBookings(
                room,
                bookingRequest.getCheckInTimestamp(),
                bookingRequest.getCheckOutTimestamp()
        );
        if (!conflictingBookings.isEmpty()) {
            throw new IllegalStateException("The selected room is already booked for the specified time period.");
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

        Booking savedBooking = bookingRepository.save(booking);

        RoomAvailability roomAvailability = RoomAvailability.builder()
                .room(room)
                .booking(savedBooking)
                .startDate(bookingRequest.getCheckInTimestamp())
                .endDate(bookingRequest.getCheckOutTimestamp())
                .build();
        roomAvailabilityRepository.save(roomAvailability);

        return savedBooking;
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
            booking.getRoom().setBookRoom();
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
                booking.getRoom().reopenRoom();
                roomAvailabilityRepository.deleteByBooking(booking);
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

            // Check if dates are being updated and validate availability
            boolean datesChanged =
                    !updateBooking.getCheckInTimestamp().equals(booking.getCheckInTimestamp()) ||
                            !updateBooking.getCheckOutTimestamp().equals(booking.getCheckOutTimestamp());

            if (datesChanged) {
                // Check for conflicts in RoomAvailability
                List<RoomAvailability> conflicts = roomAvailabilityRepository.findConflictingBookings(
                        room,
                        updateBooking.getCheckInTimestamp(),
                        updateBooking.getCheckOutTimestamp()
                );

                if (!conflicts.isEmpty() && !conflicts.stream().allMatch(ra -> ra.getBooking().getId().equals(id))) {
                    throw new IllegalArgumentException("Room is already booked for the given time period.");
                }
            }

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

            // Save the updated booking
            Booking updatedBooking = bookingRepository.save(booking);

            // Update RoomAvailability if dates changed
            if (datesChanged) {
                RoomAvailability availability = roomAvailabilityRepository.findByBooking(booking)
                        .orElseThrow(() -> new EntityNotFoundException("RoomAvailability not found for booking id: " + id));

                availability.setStartDate(booking.getCheckInTimestamp());
                availability.setEndDate(booking.getCheckOutTimestamp());
                roomAvailabilityRepository.save(availability);
            }

            return updatedBooking;
        }
        throw new EntityNotFoundException("Booking not found with id: " + id);
    }

    @Override
    public void extendBooking(Long id, Long newCheckOutTimestamp) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            if (booking.getStatus() == BookingStatus.COMPLETED || booking.getStatus() == BookingStatus.CANCELED) {
                throw new IllegalStateException("Booking cannot be extended");
            }

            if (newCheckOutTimestamp <= booking.getCheckOutTimestamp()) {
                throw new IllegalArgumentException("New check-out date must be after the current check-out date.");
            }
            // Update the booking's check-out timestamp
            booking.setCheckOutTimestamp(newCheckOutTimestamp);

            // Update the associated RoomAvailability
            RoomAvailability availability = roomAvailabilityRepository.findByBooking(booking)
                    .orElseThrow(() -> new EntityNotFoundException("RoomAvailability not found for booking id: " + id));

            availability.setEndDate(newCheckOutTimestamp);
            roomAvailabilityRepository.save(availability);

            bookingRepository.save(booking);
        }
        throw new EntityNotFoundException("Booking not found with id: " + id);
    }

    @Override
    public void softDeleteById(Long id) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();

            // Soft delete the booking
            booking.onDelete();

            // Remove the associated RoomAvailability
            RoomAvailability availability = roomAvailabilityRepository.findByBooking(booking)
                    .orElseThrow(() -> new EntityNotFoundException("RoomAvailability not found for booking id: " + id));
            roomAvailabilityRepository.delete(availability);

            bookingRepository.save(booking);
        } else {
            throw new EntityNotFoundException("Booking not found with id: " + id);
        }
    }

}
