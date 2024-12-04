package com.wander_book.service;

import com.wander_book.model.service_provide.ServiceProvide;
import com.wander_book.dto.request.service.SimpleService;
import com.wander_book.service.Common.IBaseService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IServiceProvideService extends IBaseService<ServiceProvide> {
    // Check if a service exists by ID
    boolean existsById(Long id);

    ServiceProvide addService(SimpleService simpleService);
    // Find a service by its name
    Optional<ServiceProvide> findByServiceName(String serviceName);

    // Find services within a specific price range
    List<ServiceProvide> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    // Save a new or updated service
    ServiceProvide saveService(Long id, SimpleService updateService);

    // Delete a service by ID (soft delete can be applied here if needed)
    void deleteById(Long id);
}
