package com.wander_book.repository;

import com.wander_book.model.BookedRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<BookedRoom, Long> {
}
