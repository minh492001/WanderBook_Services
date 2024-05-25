package com.wander_book.service.impl;

import com.wander_book.model.BookedRoom;
import com.wander_book.repository.BookingRepository;
import com.wander_book.service.IBookingService;
import com.wander_book.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {
    private final BookingRepository bookingRepository;
    private final IRoomService roomService;

    @Override
    public List<BookedRoom> getAllBookings() {
        return bookingRepository.findAll();
    }
}
