package com.wander_book.model;

import com.wander_book.model.comon.BaseEntity;
import com.wander_book.model.room.Room;
import com.wander_book.model.service_provide.ServiceProvide;
import com.wander_book.model.service_provide.ServiceReservation;
import com.wander_book.model.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // The user who created the booking

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room; // The room that is being booked

    @Column(name = "check_in_timestamp", nullable = false)
    private Long checkInTimestamp; // Check-in time stored as a UNIX timestamp (in milliseconds)

    @Column(name = "check_out_timestamp", nullable = false)
    private Long checkOutTimestamp; // Check-out time stored as a UNIX timestamp (in milliseconds)

    @Column(name = "adults_count", nullable = false)
    private int adultsCount; // Number of adults

    @Column(name = "children_count")
    private int childrenCount; // Number of children

    @Column(name = "total_guests", nullable = false)
    private int totalGuests; // Total number of guests

    @Column(name = "confirmation_code", nullable = false, unique = true)
    private String confirmationCode; // Unique confirmation code for the booking

    public Booking(User user, Room room, Long checkInTimestamp, Long checkOutTimestamp, int adultsCount, int childrenCount) {
        super();
        this.user = user;
        this.room = room;
        this.checkInTimestamp = checkInTimestamp;
        this.checkOutTimestamp = checkOutTimestamp;
        this.adultsCount = adultsCount;
        this.childrenCount = childrenCount;
        this.totalGuests = adultsCount + childrenCount;
        setConfirmationCode();
    }

    public Booking createBooking(Booking booking) {
        // Update the room state to "Booked"
        if (booking.room != null) {
            booking.room.bookRoom();
        }
        return booking;
    }

    public void cancelBooking() {
        // Update the room state back to "Open" if the booking is canceled
        if (room != null) {
            room.cancelBooking();
        }
        onDelete();
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

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServiceReservation> bookingServices = new ArrayList<>();

    // Method to add service
    public void addReservationService(ServiceProvide serviceProvide, int quantity) {
        ServiceReservation bookingService = ServiceReservation.createBookingService(this, serviceProvide, quantity);
        bookingServices.add(bookingService);
    }

    // Method to remove service
    public void removeReservationService(ServiceReservation bookingService) {
        bookingServices.remove(bookingService);
        bookingService.setDeletedAt(System.currentTimeMillis());
    }

}
