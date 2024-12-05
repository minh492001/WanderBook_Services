package com.wander_book.controller;


import com.wander_book.dto.request.service.ServiceProvideRequestDTO;
import com.wander_book.dto.request.service.SimpleServiceDTO;
import com.wander_book.model.service_provide.ServiceProvide;
import com.wander_book.dto.request.service.ServiceProvideResponseDTO;
import com.wander_book.service.IServiceProvideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v2/services")
@RequiredArgsConstructor
public class ServiceProvideController {
    private final IServiceProvideService serviceProvideService;

    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("hello");
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @GetMapping("/all")
    public ResponseEntity<List<ServiceProvideResponseDTO>> getAllServicesAsResponseDTO() {
        List<ServiceProvideResponseDTO> services = serviceProvideService.getAllServicesAsResponseDTO();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/all-names")
    public ResponseEntity<List<SimpleServiceDTO>> getAllServicesAsSimpleDTO() {
        List<SimpleServiceDTO> services = serviceProvideService.getAllServicesAsSimpleDTO();
        return ResponseEntity.ok(services);
    }

    // Get service by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceProvideService.getServiceById(id));
    }

    // Get services within a price range
    @GetMapping("/price-range")
    public ResponseEntity<List<ServiceProvideResponseDTO>> findByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        return ResponseEntity.ok(serviceProvideService.findByPriceRange(minPrice, maxPrice));
    }

    // Check if a service exists by ID
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> serviceExists(@PathVariable Long id) {
        boolean exists = serviceProvideService.existsById(id);
        return ResponseEntity.ok(exists);
    }

    // Get a service by name
    @GetMapping("/name")
    public ResponseEntity<Optional<ServiceProvideResponseDTO>> findByServiceName(@RequestParam String serviceName) {
        Optional<ServiceProvideResponseDTO> service = serviceProvideService.findByServiceName(serviceName);

        if (service.isPresent()) {
            return ResponseEntity.ok(service);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Optional.empty());
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ServiceProvideResponseDTO> addService(@RequestBody ServiceProvideRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceProvideService.addService(request));
    }

    // Create or update a service
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ServiceProvideResponseDTO> updateService(@PathVariable Long id, @RequestBody ServiceProvideRequestDTO request) {
        return ResponseEntity.ok(serviceProvideService.updateService(id, request));
    }

    // Delete a service by ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteServiceById(@PathVariable Long id) {
        serviceProvideService.deleteById(id);
        return ResponseEntity.ok("Service deleted successfully.");
    }
}
