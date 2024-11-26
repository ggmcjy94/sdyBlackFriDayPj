package com.example.OrderService.service;

import com.example.OrderService.dto.*;
import com.example.OrderService.entity.ProductOrder;
import com.example.OrderService.enums.OrderStatus;
import com.example.OrderService.feign.CatalogClient;
import com.example.OrderService.feign.DeliveryClient;
import com.example.OrderService.feign.PaymentClient;
import com.example.OrderService.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {


    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CatalogClient catalogClient;
    @Autowired
    DeliveryClient deliveryClient;
    @Autowired
    PaymentClient paymentClient;



    public StartOrderResponseDto startOrder(Long userId, Long productId, Long count) {
        var product = catalogClient.getProduct(productId);
        var paymentMethod = paymentClient.getPaymentMethod(userId);
        var address = deliveryClient.getUserAddress(userId);

        ProductOrder order = new ProductOrder(
                userId,
                productId,
                count,
                OrderStatus.INITIATED,
                null,
                null
        );

        orderRepository.save(order);
        StartOrderResponseDto startOrderResponseDto = new StartOrderResponseDto();
        startOrderResponseDto.orderId = order.id;
        startOrderResponseDto.paymentMethod = paymentMethod;
        startOrderResponseDto.address = address;
        return startOrderResponseDto;
    }


    public ProductOrder finishOrder(Long orderId, Long paymentMethodId, Long addressId) {
        ProductOrder order = orderRepository.findById(orderId).orElseThrow();

        var product = catalogClient.getProduct(order.productId);

        ProcessPaymentDto processPaymentDto = new ProcessPaymentDto();
        processPaymentDto.orderId = order.id;
        processPaymentDto.userId = order.userId;
        processPaymentDto.amountKRW = Long.parseLong(product.get("price").toString()) * order.count;
        processPaymentDto.paymentMethodId = paymentMethodId;

        var payment = paymentClient.processPayment(processPaymentDto);

        var address = deliveryClient.getAddress(addressId);

        ProcessDeliveryDto processDeliveryDto = new ProcessDeliveryDto();
        processDeliveryDto.orderId = order.id;
        processDeliveryDto.productName = product.get("name").toString();
        processDeliveryDto.productCount = order.count;
        processDeliveryDto.address = address.get("address").toString();
        var delivery = deliveryClient.registerDelivery(processDeliveryDto);


        DecreaseStockCountDto decreaseStockCountDto = new DecreaseStockCountDto();
        decreaseStockCountDto.decreaseCount = order.count;
        catalogClient.decreaseStockCount(order.productId,decreaseStockCountDto);

        order.paymentId = Long.parseLong(payment.get("id").toString());
        order.deliveryId = Long.parseLong(delivery.get("id").toString());
        order.orderStatus = OrderStatus.DELIVERY_REQUESTED;

        return orderRepository.save(order);
    }

    public List<ProductOrder> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public ProductOrderDetailDto getOrderDetail (Long orderId) {
        var order = orderRepository.findById(orderId).orElseThrow();

        var paymentRes = paymentClient.getPayment(order.paymentId);
        var deliveryRes = deliveryClient.getDelivery(order.deliveryId);

        var dto = new ProductOrderDetailDto(
                order.id,
                order.userId,
                order.productId,
                order.paymentId,
                order.deliveryId,
                order.orderStatus,
                paymentRes.get("paymentStatus").toString(),
                deliveryRes.get("status").toString()
        );
        return  dto;
    }

}
