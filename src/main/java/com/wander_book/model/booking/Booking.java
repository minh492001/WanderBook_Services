package com.wander_book.model.booking;

import com.wander_book.model.comon.BaseEntity;
import com.wander_book.model.room.Room;
import com.wander_book.model.room.RoomState;
import com.wander_book.model.service_provide.ServiceReservation;
import com.wander_book.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "bookings")
public class Booking extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    @ToString.Exclude
    private Room room;

    @Column(nullable = false)
    @NotNull(message = "Check-in timestamp must not be null")
    private Long checkInTimestamp; // Check-in time stored as a UNIX timestamp (in milliseconds)

    @Column(nullable = false)
    @NotNull(message = "Check-out timestamp must not be null")
    private Long checkOutTimestamp; // Check-out time stored as a UNIX timestamp (in milliseconds)

    @Column(nullable = false)
    private int adultsCount; // Number of adults

    @Column(nullable = false)
    private int childrenCount; // Number of children

    @Column(nullable = false)
    private int totalGuests; // Total number of guests

    @Column(nullable = false, unique = true)
    private String confirmationCode; // Unique confirmation code for the booking

    @Column(length = 500)
    private String notes;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServiceReservation> bookingServices = new ArrayList<>();

    @Override
    protected void beforePersist() {
        this.totalGuests = this.adultsCount + this.childrenCount;
        if (this.status == null) {
            this.status = BookingStatus.PENDING;
        }
        if (this.confirmationCode == null) {
            this.confirmationCode = RandomStringUtils.randomNumeric(10);
        }
        if (this.room != null) {
            this.room.changeState(RoomState.BOOKED);
        }
    }

    @Override
    protected void beforeUpdate() {
        validateTimestamps();
    }

    private void validateTimestamps() {
        if (this.checkInTimestamp == null || this.checkOutTimestamp == null) {
            throw new IllegalArgumentException("Check-in and Check-out timestamps must not be null.");
        }
        if (this.checkInTimestamp >= this.checkOutTimestamp) {
            throw new IllegalArgumentException("Check-out time must be after check-in time.");
        }
    }

    public void updateGuestCount(int newAdultsCount, int newChildrenCount) {
        this.adultsCount = newAdultsCount;
        this.childrenCount = newChildrenCount;
        this.totalGuests = newAdultsCount + newChildrenCount;
        onUpdate();
    }

    private void setConfirmationCode() {
        this.confirmationCode =  RandomStringUtils.randomNumeric(10);
    }
}
