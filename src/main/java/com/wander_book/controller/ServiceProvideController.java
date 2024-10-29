package com.wander_book.controller;


import com.wander_book.model.serviceProvide.ServiceProvide;
import com.wander_book.request.service.SimpleService;
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
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
public class ServiceProvideController {
    private final IServiceProvideService serviceProvideService;

    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("hello");
    }

    // Get all services
    @GetMapping("/all")
    public ResponseEntity<List<ServiceProvide>> getAllServices() {
        List<ServiceProvide> services = serviceProvideService.findAll();
        return ResponseEntity.ok(services);
    }

    // Get service by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getServiceById(@PathVariable Long id) {
        try {
            Optional<ServiceProvide> service = serviceProvideService.findByIdAndNotDeleted(id);
            return ResponseEntity.ok(service);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching service");
        }
    }

    // Get services within a price range
    @GetMapping("/price-range")
    public ResponseEntity<List<ServiceProvide>> getServicesByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<ServiceProvide> services = serviceProvideService.findByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(services);
    }

    // Check if a service exists by ID
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> serviceExists(@PathVariable Long id) {
        boolean exists = serviceProvideService.existsById(id);
        return ResponseEntity.ok(exists);
    }

    // Get a service by name
    @GetMapping("/name/{serviceName}")
    public ResponseEntity<?> getServiceByName(@PathVariable("serviceName") String serviceName) {
        try {
            Optional<ServiceProvide> service = serviceProvideService.findByServiceName(serviceName);
            return ResponseEntity.ok(service);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching service");
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ServiceProvide> addService(@RequestBody SimpleService simpleService) {
        ServiceProvide newService = serviceProvideService.addService(simpleService);
        return ResponseEntity.status(HttpStatus.CREATED).body(newService);
    }

    // Create or update a service
    @PostMapping("/{id}")
    public ResponseEntity<?> saveService(@PathVariable Long id, @RequestBody SimpleService serviceEdit) {
        try {
            ServiceProvide updated = serviceProvideService.saveService(id, serviceEdit);
            return ResponseEntity.status(HttpStatus.CREATED).body(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while updating the service");
        }
    }

    // Delete a service by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteServiceById(@PathVariable Long id) {
        serviceProvideService.deleteById(id);
        return ResponseEntity.ok("Service deleted successfully.");
    }
}
