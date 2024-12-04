package com.wander_book.repository.comon;

import com.wander_book.model.comon.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean // This ensures Spring doesn’t instantiate BaseRepository directly.
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Long> {

    List<T> findByDeletedAtIsNull();

    Optional<T> findById(long id);

    Optional<T> findByIdAndDeletedAtIsNull(long id);

    default void softDelete(T entity) {
        entity.onDelete();
        save(entity);
    }
}
