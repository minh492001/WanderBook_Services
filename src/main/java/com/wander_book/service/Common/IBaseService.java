package com.wander_book.service.Common;

import com.wander_book.model.comon.BaseEntity;

import java.util.List;
import java.util.Optional;

public interface IBaseService<T extends BaseEntity> {

    Optional<T> findById(Long id);

    Optional<T> findByIdAndNotDeleted(Long id);

    List<T> findAll();

    List<T> findByCreatedAt(Long createdAt);

    List<T> findByUpdatedAt(Long updatedAt);

    T save(T entity);

    void softDelete(T entity);
}
