package com.wander_book.model.booking;

import com.wander_book.model.comon.BaseEntity;
import com.wander_book.model.room.Room;
import com.wander_book.model.service_provide.ServiceProvide;
import com.wander_book.model.service_provide.ServiceReservation;
import com.wander_book.model.user.User;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
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
    private User user; // The user who created the booking

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room; // The room that is being booked

    @Column(nullable = false)
    private Long checkInTimestamp; // Check-in time stored as a UNIX timestamp (in milliseconds)

    @Column(nullable = false)
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

    public Booking(User user,
                   Room room,
                   Long checkInTimestamp,
                   Long checkOutTimestamp,
                   int adultsCount,
                   int childrenCount,
                   String notes) {
        super();
        this.user = user;
        this.room = room;
        this.checkInTimestamp = checkInTimestamp;
        this.checkOutTimestamp = checkOutTimestamp;
        this.adultsCount = adultsCount;
        this.childrenCount = childrenCount;
        this.totalGuests = adultsCount + childrenCount;
        this.notes = notes;
        this.status = BookingStatus.PENDING;
        setConfirmationCode();
        if (room != null) {
            room.prepareToBook(); // Set the room status to "Waiting"
        }
    }

    @PostConstruct
    public void initializeBooking() {
        this.onCreate();
        this.totalGuests = this.adultsCount + this.childrenCount;
        if (this.status == null) {
            this.status = BookingStatus.PENDING;
        }
        setConfirmationCode();
        if (this.room != null) {
            this.room.prepareToBook(); // Set room status to "Waiting"
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

    // Method to add service
    public void addReservationService(ServiceProvide serviceProvide, int quantity) {
        ServiceReservation bookingService = ServiceReservation.createBookingService(this, serviceProvide, quantity);
        bookingServices.add(bookingService);
    }

    // Method to remove service
    public void removeReservationService(ServiceReservation bookingService) {
        bookingServices.remove(bookingService);
        bookingService.onDelete();
    }

}
