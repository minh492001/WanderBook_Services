package com.wander_book.service.Common;

import com.wander_book.model.comon.BaseEntity;
import com.wander_book.repository.comon.BaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public abstract class BaseServiceImpl<T extends BaseEntity> implements IBaseService<T> {

    protected BaseRepository<T> repository;

    @Override
    public Optional<T> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<T> findByIdAndNotDeleted(Long id) {
        return repository.findByIdAndNotSoftDeleted(id);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public List<T> findByCreatedAt(Long createdAt) {
        return repository.findByCreatedAt(createdAt);
    }

    @Override
    public List<T> findByUpdatedAt(Long updatedAt) {
        return repository.findByUpdatedAt(updatedAt);
    }

    @Override
    public T save(T entity) {
        entity.onUpdate();
        return repository.save(entity);
    }

    @Override
    public void softDelete(T entity) {
        repository.softDelete(entity);
    }
}
