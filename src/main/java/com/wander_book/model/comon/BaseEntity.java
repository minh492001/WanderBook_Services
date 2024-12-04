package com.wander_book.model.comon;

import jakarta.persistence.*;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long createdAt;
    private Long updatedAt;
    private Long deletedAt;

    public BaseEntity() {
        onCreate();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = System.currentTimeMillis();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = System.currentTimeMillis();
    }

    @PreRemove
    public void onDelete() {
        this.deletedAt = System.currentTimeMillis();
    }
}
