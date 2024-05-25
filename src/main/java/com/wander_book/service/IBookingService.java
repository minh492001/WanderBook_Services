package com.wander_book.service;

import com.wander_book.model.BookedRoom;

import java.util.List;

public interface IBookingService {
    List<BookedRoom> getAllBookings();

}
