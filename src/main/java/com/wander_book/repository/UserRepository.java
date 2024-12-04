package com.wander_book.repository;

import com.wander_book.model.user.User;
import com.wander_book.repository.comon.BaseRepository;
import lombok.NonNull;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User> {

    boolean existsByEmail(@NonNull String email);

    Optional<User> findByEmail(@NonNull String email);

    Optional<User> findByEmailAndDeletedAtIsNull(@NonNull String email);
}
