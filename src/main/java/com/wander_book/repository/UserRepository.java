package com.wander_book.repository;

import com.wander_book.model.User;
import com.wander_book.repository.comon.BaseRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User> {

    boolean existsByEmail(@NonNull String email);

    Optional<User> findByEmail(@NonNull String email);

    // Additional helper query to retrieve non-soft-deleted users by ID
    Optional<User> findByIdAndDeletedAtIsNull(@NonNull Long id);

    @Transactional
    default void deleteByEmail(@NonNull String email) {
        findByEmail(email).ifPresent(this::softDelete);
    }

    // Override deleteById for soft delete by setting the deletedAt timestamp

    @Transactional
    default void deleteById(@NonNull Long id) {
        findByIdAndDeletedAtIsNull(id).ifPresent(this::softDelete);
    }

    long countByDateOfBirthLessThan(@NonNull Long timestamp);

    // Helper method to count users by age
    default long countByAge(int age) {
        return findAll().stream()
                .filter(user -> user.getDeletedAt() == null)
                .filter(user -> {
                    int currentYear = java.time.Year.now().getValue();
                    int birthYear = java.time.Instant.ofEpochMilli(user.getDateOfBirth())
                            .atZone(java.time.ZoneId.systemDefault())
                            .getYear();
                    return (currentYear - birthYear) == age;
                }).count();
    }
}
