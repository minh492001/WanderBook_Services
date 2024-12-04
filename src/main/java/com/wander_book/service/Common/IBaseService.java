package com.wander_book.service.Common;

import com.wander_book.model.comon.BaseEntity;

import java.util.Optional;

public interface IBaseService<T extends BaseEntity> {

    Optional<T> findById(Long id);

    Optional<T> findByIdAndNotDeleted(Long id);

    T save(T entity);

    void delete(T entity);

    void softDelete(T entity);
}
