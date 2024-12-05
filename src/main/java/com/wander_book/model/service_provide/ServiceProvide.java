package com.wander_book.model.service_provide;

import com.wander_book.model.comon.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceProvide extends BaseEntity {

    private String serviceName;
    @Lob
    private String description;
    private BigDecimal price;

    @OneToMany(mappedBy = "serviceProvide", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServiceReservation> bookings = new ArrayList<>();
}
