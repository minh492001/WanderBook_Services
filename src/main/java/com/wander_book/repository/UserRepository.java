package com.wander_book.repository;

import com.wander_book.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    default Optional<User> findById(Long id) {
        return findByIdAndDeletedAtIsNull(id);
    }
    // Additional helper query to retrieve non-soft-deleted users by ID
    Optional<User> findByIdAndDeletedAtIsNull(Long id);

    @Transactional
    default void deleteByEmail(String email) {
        findByEmail(email).ifPresent(user -> {
            user.setDeletedAt(System.currentTimeMillis());
            save(user);
        });
    }

    // Override deleteById for soft delete by setting the deletedAt timestamp

    @Transactional
    default void deleteById(Long id) {
        findByIdAndDeletedAtIsNull(id).ifPresent(user -> {
            user.setDeletedAt(System.currentTimeMillis());
            save(user);
        });
    }

    long countByDateOfBirthLessThan(Long timestamp);

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
