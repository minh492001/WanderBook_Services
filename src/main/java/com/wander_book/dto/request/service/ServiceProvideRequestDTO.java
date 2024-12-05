package com.wander_book.dto.request.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceProvideRequestDTO {
    private String serviceName;
    private String description;
    private BigDecimal price;
}
