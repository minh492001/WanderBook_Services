package com.wander_book.service.impl;

import com.wander_book.model.serviceProvide.ServiceProvide;
import com.wander_book.repository.ServiceProvideRepository;
import com.wander_book.request.service.SimpleService;
import com.wander_book.service.Common.BaseServiceImpl;
import com.wander_book.service.IServiceProvideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceProvideService extends BaseServiceImpl<ServiceProvide> implements IServiceProvideService {

    private final ServiceProvideRepository serviceProvideRepository;

    @Autowired
    public ServiceProvideService(ServiceProvideRepository serviceProvideRepository) {
        this.repository = serviceProvideRepository;
        this.serviceProvideRepository = serviceProvideRepository;
    }

    @Override
    public boolean existsById(Long id) {
        return serviceProvideRepository.existsById(id);
    }

    @Override
    public ServiceProvide addService(SimpleService simpleService) {
        ServiceProvide serviceProvide = new ServiceProvide();
        serviceProvide.setServiceName(simpleService.getServiceName());
        serviceProvide.setDescription(simpleService.getDescription());
        serviceProvide.setPrice(simpleService.getPrice());
        return serviceProvideRepository.save(serviceProvide);
    }

    @Override
    public Optional<ServiceProvide> findByServiceName(String serviceName) {
        return serviceProvideRepository.findByServiceName(serviceName);
    }

    @Override
    public List<ServiceProvide> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return serviceProvideRepository.findByPriceBetween(minPrice, maxPrice);
    }

    @Override
    public ServiceProvide saveService(Long id, SimpleService updateService) {
        return serviceProvideRepository.findByIdAndNotSoftDeleted(id).map(existingService -> {
            if (updateService.getServiceName() != null) existingService.setServiceName(updateService.getServiceName());
            if (updateService.getDescription() != null) existingService.setDescription(updateService.getDescription());
            if (updateService.getPrice() != null) existingService.setPrice(updateService.getPrice());
            existingService.onUpdate();
            return serviceProvideRepository.save(existingService);
        }).orElseThrow(() -> new IllegalArgumentException("Service not found or has been deleted"));
    }

    @Override
    public void deleteById(Long id) {
        serviceProvideRepository.findById(id).ifPresent(serviceProvide -> {
            serviceProvide.onDelete();
            serviceProvideRepository.save(serviceProvide);
        });
    }
}
