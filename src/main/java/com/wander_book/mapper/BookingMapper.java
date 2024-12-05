package com.wander_book.mapper;

import com.wander_book.dto.request.booking.BookingRequestDTO;
import com.wander_book.dto.response.BookingResponseDTO;
import com.wander_book.model.booking.Booking;
import com.wander_book.model.booking.BookingStatus;
import com.wander_book.model.room.Room;
import com.wander_book.model.user.User;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {
    public Booking toEntity(BookingRequestDTO dto, User user, Room room) {
        return Booking.builder()
                .user(user)
                .room(room)
                .checkInTimestamp(dto.getCheckInTimestamp())
                .checkOutTimestamp(dto.getCheckOutTimestamp())
                .adultsCount(dto.getAdultsCount())
                .childrenCount(dto.getChildrenCount())
                .notes(dto.getNotes())
                .status(BookingStatus.PENDING) // Default status
                .build();
    }

    public BookingResponseDTO toDTO(Booking booking) {
        return BookingResponseDTO.builder()
                .id(booking.getId())
                .userEmail(booking.getUser().getEmail())
                .roomNumber(booking.getRoom().getRoomNumber())
                .checkInTimestamp(booking.getCheckInTimestamp())
                .checkOutTimestamp(booking.getCheckOutTimestamp())
                .adultsCount(booking.getAdultsCount())
                .childrenCount(booking.getChildrenCount())
                .totalGuests(booking.getTotalGuests())
                .notes(booking.getNotes())
                .confirmationCode(booking.getConfirmationCode())
                .status(booking.getStatus().name())
                .build();
    }
}
