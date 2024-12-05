package com.wander_book.service;

import com.wander_book.dto.request.service.ServiceProvideRequestDTO;
import com.wander_book.dto.response.ServiceProvideResponseDTO;
import com.wander_book.dto.request.service.SimpleServiceDTO;
import com.wander_book.model.service_provide.ServiceProvide;
import com.wander_book.service.Common.IBaseService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IServiceProvideService extends IBaseService<ServiceProvide> {
    boolean existsById(Long id);
    List<ServiceProvideResponseDTO> getAllServicesAsResponseDTO();

    List<SimpleServiceDTO> getAllServicesAsSimpleDTO();

    Optional<ServiceProvideResponseDTO> findByServiceName(String serviceName);

    ServiceProvideResponseDTO getServiceById(Long id);

    List<ServiceProvideResponseDTO> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    ServiceProvideResponseDTO addService(ServiceProvideRequestDTO request);

    ServiceProvideResponseDTO updateService(Long id, ServiceProvideRequestDTO request);

    void deleteById(Long id);
}
