package com.wander_book.model.comon;

import jakarta.persistence.*;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long createdAt; // UNIX timestamp for created_at
    private Long updatedAt; // UNIX timestamp for updated_at
    private Long deletedAt; // UNIX timestamp for deleted_at (soft delete)

    // Constructor that calls the preCreate method
    public BaseEntity() {
        onCreate();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = System.currentTimeMillis();
        // this.createdAt = Instant.now().toEpochMilli();
        // providing more consistent behavior when used in applications that handle time zones and other time-based operations
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = System.currentTimeMillis();
    }

    @PreRemove
    public void onDelete() {
        this.deletedAt = System.currentTimeMillis(); // Mark deletion timestamp for soft delete
    }
}
