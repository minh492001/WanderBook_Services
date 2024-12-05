package com.wander_book.repository;

import com.wander_book.model.service_provide.ServiceProvide;
import com.wander_book.repository.comon.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ServiceProvideRepository extends BaseRepository<ServiceProvide> {

    Optional<ServiceProvide> findByServiceName(String serviceName);

    @Query("SELECT s FROM ServiceProvide s WHERE s.price BETWEEN :minPrice AND :maxPrice")
    List<ServiceProvide> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);
}
