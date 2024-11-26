package com.example.OrderService.controller;

import com.example.OrderService.dto.FinishOrderDto;
import com.example.OrderService.dto.ProductOrderDetailDto;
import com.example.OrderService.dto.StartOrderDto;
import com.example.OrderService.dto.StartOrderResponseDto;
import com.example.OrderService.entity.ProductOrder;
import com.example.OrderService.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {


    @Autowired
    OrderService orderService;


    @PostMapping("/order/start-order")
    public StartOrderResponseDto startOrder(@RequestBody StartOrderDto dto) {

    }

    @PostMapping("/order/finish-order")
    public StartOrderResponseDto finishOrder(@RequestBody FinishOrderDto dto) {

    }

    @GetMapping("/order/users/{userId}/orders")
    public List<ProductOrder> getUserOrders(@PathVariable Long userId) {

    }

    @GetMapping("/order/orders/{orderId}")
    public ProductOrderDetailDto getOrders(@PathVariable Long orderId) {

    }
}
