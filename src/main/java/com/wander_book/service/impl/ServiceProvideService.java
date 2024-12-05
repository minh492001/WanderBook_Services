package com.wander_book.service.impl;

import com.wander_book.dto.request.service.ServiceProvideRequestDTO;
import com.wander_book.dto.request.service.SimpleServiceDTO;
import com.wander_book.mapper.ServiceProvideMapper;
import com.wander_book.model.service_provide.ServiceProvide;
import com.wander_book.repository.ServiceProvideRepository;
import com.wander_book.dto.request.service.ServiceProvideResponseDTO;
import com.wander_book.service.Common.BaseServiceImpl;
import com.wander_book.service.IServiceProvideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceProvideService extends BaseServiceImpl<ServiceProvide> implements IServiceProvideService {

    private final ServiceProvideRepository serviceProvideRepository;
    private final ServiceProvideMapper serviceProvideMapper;

    @Autowired
    public ServiceProvideService(ServiceProvideRepository serviceProvideRepository, ServiceProvideMapper serviceProvideMapper) {
        this.repository = serviceProvideRepository;
        this.serviceProvideRepository = serviceProvideRepository;
        this.serviceProvideMapper = serviceProvideMapper;
    }

    @Override
    public boolean existsById(Long id) {
        return serviceProvideRepository.existsById(id);
    }

    @Override
    public List<ServiceProvideResponseDTO> getAllServicesAsResponseDTO() {
        return serviceProvideRepository.findAll()
                .stream()
                .map(serviceProvideMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SimpleServiceDTO> getAllServicesAsSimpleDTO() {
        return serviceProvideRepository.findAll()
                .stream()
                .map(serviceProvideMapper::toSimpleDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ServiceProvideResponseDTO> findByServiceName(String serviceName) {
        return serviceProvideRepository.findByServiceName(serviceName)
                .map(serviceProvideMapper::toDTO);
    }

    @Override
    public ServiceProvideResponseDTO getServiceById(Long id) {
        ServiceProvide serviceProvide = serviceProvideRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("Service with ID: " + id + " not found or has been deleted."));
        return serviceProvideMapper.toDTO(serviceProvide);
    }

    @Override
    public List<ServiceProvideResponseDTO> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return serviceProvideRepository.findByPriceRange(minPrice, maxPrice)
                .stream()
                .map(serviceProvideMapper::toDTO)
                .toList();
    }

    @Override
    public ServiceProvideResponseDTO addService(ServiceProvideRequestDTO request) {
        ServiceProvide serviceProvide = serviceProvideMapper.toEntity(request);
        ServiceProvide savedService = serviceProvideRepository.save(serviceProvide);
        return serviceProvideMapper.toDTO(savedService);
    }

    @Override
    public ServiceProvideResponseDTO updateService(Long id, ServiceProvideRequestDTO request) {
        ServiceProvide serviceProvide = serviceProvideRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service with ID: " + id + " not found."));

        serviceProvideMapper.updateEntityFromDTO(request, serviceProvide);

        ServiceProvide updatedService = serviceProvideRepository.save(serviceProvide);
        return serviceProvideMapper.toDTO(updatedService);
    }
        @Override
    public void deleteById(Long id) {
        serviceProvideRepository.findById(id).ifPresent(serviceProvideRepository::softDelete);
    }
}
