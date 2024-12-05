package com.wander_book.mapper;

import com.wander_book.dto.request.service.ServiceProvideRequestDTO;
import com.wander_book.dto.response.ServiceProvideResponseDTO;
import com.wander_book.dto.request.service.SimpleServiceDTO;
import com.wander_book.model.service_provide.ServiceProvide;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class ServiceProvideMapper {
    // Convert Entity -> DTO (Giải mã description từ Base64)
    public ServiceProvideResponseDTO toDTO(ServiceProvide serviceProvide) {
        String description = serviceProvide.getDescription();
        String decodedDescription = null;

        if (description != null) {
            try {
                decodedDescription = new String(Base64.getDecoder().decode(description));
            } catch (IllegalArgumentException e) {
                decodedDescription = description;
            }
        }

        return ServiceProvideResponseDTO.builder()
                .id(serviceProvide.getId())
                .serviceName(serviceProvide.getServiceName())
                .description(decodedDescription)
                .price(serviceProvide.getPrice())
                .build();
    }

    // Convert DTO -> Entity (Mã hóa description sang Base64)
    public ServiceProvide toEntity(ServiceProvideRequestDTO dto) {
        String description = dto.getDescription();
        String encodedDescription = null;

        if (description != null) {
            try {
                Base64.getDecoder().decode(description);
                encodedDescription = description; // Nếu đã là Base64, giữ nguyên
            } catch (IllegalArgumentException e) {
                encodedDescription = Base64.getEncoder().encodeToString(description.getBytes());
            }
        }

        return ServiceProvide.builder()
                .serviceName(dto.getServiceName())
                .description(encodedDescription)
                .price(dto.getPrice())
                .build();
    }

    // Update Entity from DTO (Mã hóa description nếu cần)
    public void updateEntityFromDTO(ServiceProvideRequestDTO dto, ServiceProvide serviceProvide) {
        if (dto.getServiceName() != null) {
            serviceProvide.setServiceName(dto.getServiceName());
        }
        if (dto.getDescription() != null) {
            serviceProvide.setDescription(Base64.getEncoder().encodeToString(dto.getDescription().getBytes()));
        }
        if (dto.getPrice() != null) {
            serviceProvide.setPrice(dto.getPrice());
        }
    }

    public SimpleServiceDTO toSimpleDTO(ServiceProvide serviceProvide) {
        SimpleServiceDTO dto = new SimpleServiceDTO();
        dto.setId(serviceProvide.getId());
        dto.setName(serviceProvide.getServiceName());
        return dto;
    }
}
