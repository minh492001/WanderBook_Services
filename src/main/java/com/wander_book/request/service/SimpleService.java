package com.wander_book.request.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleService {
    private String serviceName;
    private String description;
    private BigDecimal price;
}
