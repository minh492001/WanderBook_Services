package com.wander_book.model.service_provide;

import com.wander_book.model.booking.Booking;
import com.wander_book.model.comon.BaseEntity;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "service_reservations")
public class ServiceReservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceProvide serviceProvide;

    private int quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;

    @PostConstruct
    public void initializeServiceReservation() {
        this.onCreate();
        this.totalPrice =  this.price.multiply(BigDecimal.valueOf(this.quantity));
    }
}
