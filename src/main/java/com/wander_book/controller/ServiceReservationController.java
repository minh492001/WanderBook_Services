package com.wander_book.controller;

import com.wander_book.model.service_provide.ServiceReservation;
import com.wander_book.service.IServiceReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v2/service-reservations")
@RequiredArgsConstructor
public class ServiceReservationController {
    private final IServiceReservationService serviceReservationService;

    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("hello");
    }

    @PostMapping
    public ResponseEntity<ServiceReservation> addServiceToBooking(
            @RequestParam Long bookingId,
            @RequestParam Long serviceId,
            @RequestParam int quantity) {
        ServiceReservation serviceReservation = serviceReservationService.addServiceToBooking(bookingId, serviceId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceReservation);
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<ServiceReservation>> getServicesByBooking(@PathVariable Long bookingId) {
        List<ServiceReservation> services = serviceReservationService.getServicesByBooking(bookingId);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/booking/{bookingId}/total-price")
    public ResponseEntity<BigDecimal> getTotalPriceByBooking(@PathVariable Long bookingId) {
        BigDecimal totalPrice = serviceReservationService.getTotalPriceByBooking(bookingId);
        return ResponseEntity.ok(totalPrice);
    }

    // Xóa một dịch vụ đi kèm khỏi booking
    @DeleteMapping("/{serviceReservationId}")
    public ResponseEntity<Void> deleteServiceReservation(@PathVariable Long serviceReservationId) {
        serviceReservationService.deleteServiceReservation(serviceReservationId);
        return ResponseEntity.noContent().build();
    }
}
