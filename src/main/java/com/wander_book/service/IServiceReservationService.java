package com.wander_book.service;

import com.wander_book.model.service_provide.ServiceReservation;
import com.wander_book.service.Common.IBaseService;

import java.math.BigDecimal;
import java.util.List;

public interface IServiceReservationService extends IBaseService<ServiceReservation> {

    ServiceReservation addServiceToBooking(Long bookingId, Long serviceId, int quantity);

    List<ServiceReservation> getServicesByBooking(Long bookingId);

    BigDecimal getTotalPriceByBooking(Long bookingId);

    void deleteServiceReservation(Long serviceReservationId);
}
