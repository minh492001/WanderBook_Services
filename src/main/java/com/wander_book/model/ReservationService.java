package com.wander_book.model;

import com.wander_book.model.comon.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class ReservationService extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    private int serviceQuantity;
    private BigDecimal price;

    public ReservationService(Booking booking, Service service, int serviceQuantity, BigDecimal price) {
        this.booking = booking;
        this.service = service;
        this.serviceQuantity = serviceQuantity;
        this.price = price;
        this.setCreatedAt(System.currentTimeMillis());
    }

    public static ReservationService createBookingService(Booking booking, Service service, int quantity) {
        return new ReservationService(booking, service, quantity, service.getPrice().multiply(BigDecimal.valueOf(quantity)));
    }
}
