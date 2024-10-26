package com.wander_book.model;

import com.wander_book.model.comon.BaseEntity;
import com.wander_book.model.enums.PaymentMethod;
import com.wander_book.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(nullable = false)
    private BigDecimal roomAmount; // Tổng tiền phải trả từ phòng

    @Column(nullable = false)
    private BigDecimal serviceAmount; // Tổng tiền phải trả từ dịch vụ

    @Column(nullable = false)
    private int totalServicesUsed; // Tổng số dịch vụ đã sử dụng

    private Long paymentTime; // Thời gian thanh toán (UNIX timestamp)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod; // Hình thức thanh toán (Credit Card, Cash, Online Transfer, etc.)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus; // Tình trạng thanh toán (Paid, Pending, Failed)

    // Phương thức để cập nhật thời gian thanh toán
    public void setPaymentTime(Long paymentTime) {
        this.paymentTime = paymentTime != null ? paymentTime : System.currentTimeMillis();
    }

    // Phương thức để cập nhật trạng thái thanh toán
    public void updatePaymentStatus(PaymentStatus status) {
        this.paymentStatus = status;
    }

    // Phương thức để tính tổng tiền thanh toán (có thể dùng khi tạo Payment mới)
    public BigDecimal calculateTotalAmount() {
        return roomAmount.add(serviceAmount);
    }
}
