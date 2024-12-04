package com.wander_book.service.Common;

import com.wander_book.model.comon.BaseEntity;
import com.wander_book.repository.comon.BaseRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public abstract class BaseServiceImpl<T extends BaseEntity> implements IBaseService<T> {

    protected  BaseRepository<T> repository;

    @Override
    public Optional<T> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<T> findByIdAndNotDeleted(Long id) {
        return repository.findByIdAndDeletedAtIsNull(id);
    }

    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    public void delete(T entity) {
        repository.delete(entity);
    }
    @Override
    public void softDelete(T entity) {
        entity.setDeletedAt(System.currentTimeMillis());
        repository.save(entity);
    }
}
