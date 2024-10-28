package com.wander_book.repository.comon;

import com.wander_book.model.comon.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean // This ensures Spring doesn’t instantiate BaseRepository directly.
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Long> {

    // Find by ID (only if not soft-deleted)
    default Optional<T> findByIdAndNotSoftDeleted(Long id) {
        Optional<T> entity = findById(id);
        return entity.isPresent() && entity.get().getDeletedAt() == null ? entity : Optional.empty();
    }

    // Find by creation date
    List<T> findByCreatedAt(Long createdAt);

    // Find by update date
    List<T> findByUpdatedAt(Long updatedAt);

    // Soft delete (updates deletedAt timestamp)
    default void softDelete(T entity) {
        entity.onDelete();
        save(entity);
    }
}
