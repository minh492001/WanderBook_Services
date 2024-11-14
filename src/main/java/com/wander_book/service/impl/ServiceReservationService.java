package com.wander_book.service.impl;

import com.wander_book.model.booking.Booking;
import com.wander_book.model.service_provide.ServiceProvide;
import com.wander_book.model.service_provide.ServiceReservation;
import com.wander_book.repository.ServiceReservationRepository;
import com.wander_book.service.Common.BaseServiceImpl;
import com.wander_book.service.IBookingService;
import com.wander_book.service.IServiceProvideService;
import com.wander_book.service.IServiceReservationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ServiceReservationService extends BaseServiceImpl<ServiceReservation> implements IServiceReservationService {

    private final ServiceReservationRepository serviceReservationRepository;
    private final IBookingService bookingService;
    private final IServiceProvideService serviceProvideService;

    @Autowired
    public ServiceReservationService(ServiceReservationRepository serviceReservationRepository, IBookingService bookingService, IServiceProvideService serviceProvideService) {
        this.repository = serviceReservationRepository;
        this.serviceReservationRepository = serviceReservationRepository;
        this.bookingService = bookingService;
        this.serviceProvideService = serviceProvideService;
    }

    @Override
    public ServiceReservation addServiceToBooking(Long bookingId, Long serviceId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Booking booking = bookingService.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + bookingId));

        // Lấy thông tin Service từ serviceId
        ServiceProvide serviceProvide = serviceProvideService.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("Service not found with id: " + serviceId));

        // Tính giá và tạo ServiceReservation mới
        BigDecimal price = serviceProvide.getPrice();
        BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(quantity));

        ServiceReservation serviceReservation = ServiceReservation.builder()
                .booking(booking)
                .serviceProvide(serviceProvide)
                .quantity(quantity)
                .price(price)
                .totalPrice(totalPrice)
                .build();

        serviceReservation.initializeServiceReservation();

        return serviceReservationRepository.save(serviceReservation);
    }

    @Override
    public List<ServiceReservation> getServicesByBooking(Long bookingId) {

        Booking booking = bookingService.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + bookingId));

        return serviceReservationRepository.findByBooking(booking);
    }

    @Override
    public BigDecimal getTotalPriceByBooking(Long bookingId) {
        Booking booking = bookingService.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + bookingId));

        return serviceReservationRepository.sumTotalPriceByBooking(booking);

    }

    @Override
    public void deleteServiceReservation(Long serviceReservationId) {
        if (!serviceReservationRepository.existsById(serviceReservationId)) {
            throw new EntityNotFoundException("ServiceReservation not found with id: " + serviceReservationId);
        }

        serviceReservationRepository.deleteById(serviceReservationId);
    }
}
