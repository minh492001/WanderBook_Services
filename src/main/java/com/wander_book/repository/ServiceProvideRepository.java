package com.wander_book.repository;

import com.wander_book.model.service_provide.ServiceProvide;
import com.wander_book.repository.comon.BaseRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ServiceProvideRepository extends BaseRepository<ServiceProvide> {

    Optional<ServiceProvide> findByServiceName(String serviceName);

    List<ServiceProvide> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

}
