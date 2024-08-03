package com.labhesh.secondhandstore.controllers;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.labhesh.secondhandstore.enums.OrderStatus;
import com.labhesh.secondhandstore.exception.BadRequestException;
import com.labhesh.secondhandstore.exception.InternalServerException;
import com.labhesh.secondhandstore.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@SecurityRequirement(name = "auth")
@Tag(name = "Order")
public class OrderController {

    private final OrderService orderService;
    
    @Operation(summary = "Get my order", description = "UserRole.USER")
    @GetMapping("/my-orders")
    public ResponseEntity<?> myOrder(
        @RequestParam(required = false) OrderStatus status,
        @RequestParam(required = false) LocalDateTime startDate,
        @RequestParam(required = false) LocalDateTime endDate
    ) {
        return orderService.myOrders(status, startDate, endDate);
    }

    @Operation(summary = "Change order status", description = "UserRole.USER")
    @PutMapping("/status/{id}")
    public ResponseEntity<?> changeOrderStatus(@PathVariable String id, @RequestParam OrderStatus status) throws BadRequestException, InternalServerException {
        return orderService.changeOrderStatus(id, status);
    }

    @Operation(summary = "Get order by id", description = "UserRole.USER")
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable String id) throws BadRequestException, InternalServerException {
        return orderService.getOrderById(id);
    }
    
}
