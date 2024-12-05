package com.wander_book.service;

import com.wander_book.dto.request.booking.BookingRequestDTO;
import com.wander_book.dto.response.BookingResponseDTO;
import com.wander_book.model.booking.Booking;
import com.wander_book.model.booking.BookingStatus;
import com.wander_book.service.Common.IBaseService;

import java.util.List;

public interface IBookingService extends IBaseService<Booking> {

    /**
     * Add a new booking.
     * @param requestDTO DTO containing booking information.
     * @return BookingResponseDTO containing created booking information.
     */
    BookingResponseDTO createBooking(BookingRequestDTO requestDTO);

    /**
     * Update an existing booking by ID.
     * @param id The ID of the booking to update.
     * @param requestDTO DTO containing updated booking information.
     * @return BookingResponseDTO containing updated booking information.
     */
    BookingResponseDTO updateBooking(Long id, BookingRequestDTO requestDTO);

    /**
     * Delete a booking by ID.
     * @param id The ID of the booking to delete.
     */
    void deleteBooking(Long id);

    /**
     * Get a booking by its ID.
     * @param id The ID of the booking.
     * @return BookingResponseDTO containing the booking details.
     */
    BookingResponseDTO getBookingById(Long id);

    /**
     * Get all bookings without pagination.
     * @return List of BookingResponseDTO.
     */
    List<BookingResponseDTO> getAllBookings();

    /**
     * Get bookings by user email.
     * @param email User email.
     * @return List of BookingResponseDTO.
     */
    List<BookingResponseDTO> getBookingsByUserEmail(String email);

    /**
     * Get bookings by user ID.
     * @param userId User ID.
     * @return List of BookingResponseDTO.
     */
    List<BookingResponseDTO> getBookingsByUserId(Long userId);

    /**
     * Get bookings by booking status.
     * @param status Booking status.
     * @return List of BookingResponseDTO.
     */
    List<BookingResponseDTO> getBookingsByStatus(BookingStatus status);

    /**
     * Get active bookings for a specific room during a time period.
     * @param roomId Room ID.
     * @param start Start timestamp.
     * @param end End timestamp.
     * @return List of BookingResponseDTO.
     */
    List<BookingResponseDTO> getActiveBookingsForRoom(Long roomId, Long start, Long end);

    /**
     * Count bookings by room and status.
     * @param roomId Room ID.
     * @param status Booking status.
     * @return Count of bookings.
     */
    long countBookingsByRoomAndStatus(Long roomId, BookingStatus status);

    void extendBooking(Long id, Long newCheckOutTimestamp);
}
