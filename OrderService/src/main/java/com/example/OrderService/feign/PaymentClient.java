package com.example.OrderService.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "paymentClient", url = "http://payment-service:8080")
public interface PaymentClient {

//    @PostMapping("/payment/process-payment")
//    Map<String , Object> processPayment(@RequestBody ProcessPaymentDto dto);

    @GetMapping("/payment/users/{userId}/first-method")
    Map<String , Object> getPaymentMethod(@PathVariable Long userId);

    @GetMapping("/payment/payments/{paymentId}")
    Map<String , Object> getPayment(@PathVariable Long paymentId);
}
