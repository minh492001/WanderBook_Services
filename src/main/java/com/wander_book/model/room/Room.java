package com.wander_book.model.room;

import com.wander_book.model.branch.Branch;
import com.wander_book.model.comon.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @Lob
    @Column(columnDefinition = "CLOB")
    private String description;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] photo;

    @Enumerated(EnumType.STRING)
    private RoomState state;

    @Builder.Default
    @Transient
    private List<RoomAvailability> futureBookings = new ArrayList<>();

    public void changeState(RoomState newState) {
        this.state = newState;
    }
}
