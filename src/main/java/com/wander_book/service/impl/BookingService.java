package com.wander_book.service.impl;

import com.wander_book.dto.request.booking.BookingRequestDTO;
import com.wander_book.dto.response.BookingResponseDTO;
import com.wander_book.mapper.BookingMapper;
import com.wander_book.model.booking.Booking;
import com.wander_book.model.booking.BookingStatus;
import com.wander_book.model.room.Room;
import com.wander_book.model.room.RoomAvailability;
import com.wander_book.model.user.User;
import com.wander_book.repository.BookingRepository;
import com.wander_book.repository.RoomAvailabilityRepository;
import com.wander_book.service.Common.BaseServiceImpl;
import com.wander_book.service.IBookingService;
import com.wander_book.service.IRoomService;
import com.wander_book.service.IUserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.wander_book.service.Common.Utility.*;

@Service
public class BookingService extends BaseServiceImpl<Booking> implements IBookingService {

    private final BookingRepository bookingRepository;
    private final IRoomService roomService;
    private final IUserService userService;
    private final RoomAvailabilityRepository roomAvailabilityRepository;
    private final BookingMapper bookingMapper;


    public BookingService(BookingRepository bookingRepository, RoomService roomService, IUserService userService, RoomAvailabilityRepository roomAvailabilityRepository, BookingMapper bookingMapper) {
        this.roomService = roomService;
        this.userService = userService;
        this.roomAvailabilityRepository = roomAvailabilityRepository;
        this.bookingMapper = bookingMapper;
        this.repository = bookingRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO bookingRequest) {
        if (bookingRequest.getCheckOutTimestamp() - bookingRequest.getCheckInTimestamp() < 86400000L) {
            throw new IllegalArgumentException("Check-in date must be at least 1 day before check-out date.");
        }

        Room room = roomService.findById(bookingRequest.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + bookingRequest.getRoomId()));

        User user = userService.findById(bookingRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + bookingRequest.getUserId()));

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

        Booking booking = bookingMapper.toEntity(bookingRequest, user, room);
        Booking savedBooking = bookingRepository.save(booking);

        RoomAvailability roomAvailability = RoomAvailability.builder()
                .room(room)
                .booking(savedBooking)
                .startDate(bookingRequest.getCheckInTimestamp())
                .endDate(bookingRequest.getCheckOutTimestamp())
                .build();
        roomAvailabilityRepository.save(roomAvailability);

        return bookingMapper.toDTO(savedBooking);
    }

    @Override
    public BookingResponseDTO updateBooking(Long id, BookingRequestDTO updateBooking) {

        if (updateBooking.getCheckOutTimestamp() - updateBooking.getCheckInTimestamp() < 86400000L) {
            throw new IllegalArgumentException("Check-in date must be at least 1 day before check-out date.");
        }

        Room room = roomService.findById(updateBooking.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + updateBooking.getRoomId()));

        User user = userService.findById(updateBooking.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + updateBooking.getUserId()));

        if (updateBooking.getTotalGuests() > room.getMaxOccupancy()) {
            throw new IllegalArgumentException("Total guests cannot exceed room's max occupancy of " + room.getMaxOccupancy());
        }

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

            return bookingMapper.toDTO(updatedBooking);
        }
        throw new EntityNotFoundException("Booking not found with id: " + id);
    }

    @Override
    public void deleteBooking(Long id) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();

            // Soft delete the booking
            booking.onDelete();

            // Remove the associated RoomAvailability if any
            roomAvailabilityRepository.findByBooking(booking)
                    .ifPresent(roomAvailabilityRepository::delete);

            bookingRepository.save(booking);
        } else {
            throw new EntityNotFoundException("Booking not found with id: " + id);
        }
    }

    @Override
    public BookingResponseDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with ID: " + id));
        return bookingMapper.toDTO(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream().map(bookingMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingsByUserEmail(String email) {
        List<Booking> bookings = bookingRepository.findByUser_Email(email);
        return bookings.stream().map(bookingMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingsByUserId(Long userId) {
        if (!userService.existsById(userId)) {
            throw new EntityNotFoundException("User not found with ID: " + userId);
        }
        List<Booking> bookings = bookingRepository.findByUser(userService.getReferenceById(userId));
        return bookings.stream().map(bookingMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingsByStatus(BookingStatus status) {
        List<Booking> bookings = bookingRepository.findByStatus(status);
        return bookings.stream().map(bookingMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getActiveBookingsForRoom(Long roomId, Long start, Long end) {
        Room room = roomService.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with ID: " + roomId));
        List<Booking> bookings = bookingRepository.findActiveBookingsForRoomDuringPeriod(room, start, end);
        return bookings.stream().map(bookingMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public long countBookingsByRoomAndStatus(Long roomId, BookingStatus status) {
        Room room = roomService.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with ID: " + roomId));
        return bookingRepository.countByRoomAndStatus(room, status);
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

}
