package com.wander_book.model.room;

import com.wander_book.model.booking.Booking;
import com.wander_book.model.comon.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "room_availability")
public class RoomAvailability extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(nullable = false)
    private Long startDate;

    @Column(nullable = false)
    private Long endDate;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;
}
