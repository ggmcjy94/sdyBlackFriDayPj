package com.example.OrderService.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "deliveryClient", url = "http://delivery-service:8080")
public interface DeliveryClient {

    @GetMapping("/delivery/users/{userId}/first-address")
    Map<String , Object>  getUserAddress(@PathVariable Long userId);

    @GetMapping("/delivery/address/{addressId}")
    Map<String , Object> getAddress(@PathVariable Long addressId);

//    @PostMapping("/delivery/process-delivery")
//    Map<String , Object> registerDelivery(@RequestBody ProcessDeliveryDto dto);

    @GetMapping("/delivery/deliveries/{deliveryId}")
    Map<String , Object> getDelivery(@PathVariable Long deliveryId);

}
