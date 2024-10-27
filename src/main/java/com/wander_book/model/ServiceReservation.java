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
public class ServiceReservation extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceProvide serviceProvide;

    private int serviceQuantity;
    private BigDecimal price;

    public ServiceReservation(Booking booking, ServiceProvide serviceProvide, int serviceQuantity, BigDecimal price) {
        this.booking = booking;
        this.serviceProvide = serviceProvide;
        this.serviceQuantity = serviceQuantity;
        this.price = price;
        this.setCreatedAt(System.currentTimeMillis());
    }

    public static ServiceReservation createBookingService(Booking booking, ServiceProvide serviceProvide, int quantity) {
        return new ServiceReservation(booking, serviceProvide, quantity, serviceProvide.getPrice().multiply(BigDecimal.valueOf(quantity)));
    }
}
