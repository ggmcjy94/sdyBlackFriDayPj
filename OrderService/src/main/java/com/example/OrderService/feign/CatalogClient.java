package com.example.OrderService.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(name = "catalogClient", url = "http://catalog-service:8080")
public interface CatalogClient {

    @GetMapping(value = "/catalog/products/{productId}")
    Map<String , Object> getProduct(@PathVariable Long productId);

    @PostMapping(value = "/catalog/products/{productId}")
    Map<String , Object> getProduct(@PathVariable Long productId);

}
