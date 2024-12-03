package com.wander_book.model.room;

import com.wander_book.model.Branch;
import com.wander_book.model.comon.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Table(name = "rooms")
public class Room extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    private BigDecimal pricePerNight;

    private int maxOccupancy;

    //used to specify that a property should be stored as a Large Object (LOB) in the database
    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    private RoomState state;

    @Lob
    private Blob photo;

    @Transient
    private List<RoomAvailability> futureBookings = new ArrayList<>();

    public Room() {
        super();
    }

    // Constructor using the superclass constructor to set createdAt
    public Room(
            Branch branch,
            String roomNumber,
            RoomType roomType,
            BigDecimal pricePerNight,
            RoomState state,
            int maxOccupancy
    ) {
        super(); // Calls BaseEntity's constructor to set createdAt timestamp
        this.branch = branch;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.state = state;
        this.maxOccupancy = maxOccupancy;
    }

    // Methods to manage global states
    public void setBookRoom() {
        if (this.state == RoomState.OPEN) {
            this.state = RoomState.BOOKED;
        } else {
            throw new IllegalStateException("Room is not available to book.");
        }
    }

    public void setMaintenance() {
        if (this.state != RoomState.BOOKED) {
            this.state = RoomState.MAINTENANCE;
        } else {
            throw new IllegalStateException("Cannot set maintenance for a booked room.");
        }
    }

    public void closeRoom() {
        if (this.state != RoomState.BOOKED) {
            this.state = RoomState.CLOSED;
        } else {
            throw new IllegalStateException("Cannot close a booked room.");
        }
    }

    public void reopenRoom() {
        if (this.state == RoomState.CLOSED || this.state == RoomState.MAINTENANCE) {
            this.state = RoomState.OPEN;
        } else {
            throw new IllegalStateException("Room state is not suitable for reopening.");
        }
    }

    // Utility to check if the room is globally available for booking
    public boolean isGloballyAvailable() {
        return this.state == RoomState.OPEN;
    }
}
