package com.wander_book.dto.request.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProvideResponseDTO {
    private Long id;
    private String serviceName;
    private String description;
    private BigDecimal price;
}
