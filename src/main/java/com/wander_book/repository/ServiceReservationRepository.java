package com.wander_book.repository;

import com.wander_book.model.booking.Booking;
import com.wander_book.model.service_provide.ServiceReservation;
import com.wander_book.repository.comon.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ServiceReservationRepository extends BaseRepository<ServiceReservation> {

    List<ServiceReservation> findByBooking(Booking booking);

    @Query("SELECT COALESCE(SUM(sr.totalPrice), 0) FROM ServiceReservation sr WHERE sr.booking = :booking")
    BigDecimal sumTotalPriceByBooking(Booking booking);

}
