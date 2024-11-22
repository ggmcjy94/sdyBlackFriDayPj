package com.example.DeliveryService.entity;


import com.example.DeliveryService.enums.DeliveryStatus;
import jakarta.persistence.*;

@Entity
@Table(indexes = {@Index(name = "idx_orderId", columnList = "orderId")})
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;


    public Long orderId;
    public String productName;
    public Long productCount;
    public String address;
    public DeliveryStatus status;
    public Long referenceCode;

    public Delivery() {
    }

    public Delivery(Long orderId, String productName, Long productCount, String address, DeliveryStatus deliveryStatus, Long referenceCode) {
        this.orderId = orderId;
        this.productName = productName;
        this.productCount = productCount;
        this.address = address;
        this.status = deliveryStatus;
        this.referenceCode = referenceCode;
    }
}
