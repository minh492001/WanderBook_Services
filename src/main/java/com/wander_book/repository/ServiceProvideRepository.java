package com.wander_book.repository;

import com.wander_book.model.service_provide.ServiceProvide;
import com.wander_book.repository.comon.BaseRepository;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ServiceProvideRepository extends BaseRepository<ServiceProvide> {
    // Check if a service exists by ID
    boolean existsById(@NonNull Long id);

    // Find a service by its name
    Optional<ServiceProvide> findByServiceName(String serviceName);

    // Find services within a specific price range
    List<ServiceProvide> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
}
