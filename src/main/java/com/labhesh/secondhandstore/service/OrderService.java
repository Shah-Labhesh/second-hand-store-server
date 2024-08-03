package com.labhesh.secondhandstore.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.labhesh.secondhandstore.enums.OrderStatus;
import com.labhesh.secondhandstore.exception.BadRequestException;
import com.labhesh.secondhandstore.exception.InternalServerException;
import com.labhesh.secondhandstore.models.Order;
import com.labhesh.secondhandstore.repos.OrderRepo;
import com.labhesh.secondhandstore.utils.SuccessResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepo orderRepo;

    public ResponseEntity<?> allOrders() {
        return ResponseEntity.ok(orderRepo.findAll());
    }

    // change order status
    public ResponseEntity<?> changeOrderStatus(String id, OrderStatus status)
            throws BadRequestException, InternalServerException {
        try {
            Order order = orderRepo.findOrder(UUID.fromString(id))
                    .orElseThrow(() -> new BadRequestException("Order not found"));
            switch (status) {
                case SHIPPED:
                    if (order.getStatus().equals(OrderStatus.PENDING.name())) {
                        order.setStatus(OrderStatus.SHIPPED.name());
                        orderRepo.save(order);
                    } else {
                        throw new BadRequestException(
                                "Inorder to ship the order, the order should be in pending status");
                    }
                    break;
                case DELIVERED:
                    if (order.getStatus().equals(OrderStatus.SHIPPED.name())) {
                        order.setStatus(OrderStatus.DELIVERED.name());
                        orderRepo.save(order);
                    } else {
                        throw new BadRequestException(
                                "Inorder to deliver the order, the order should be in shipped status");
                    }
                    break;
                case CANCELLED:
                    if (order.getStatus().equals(OrderStatus.PENDING.name())) {
                        order.setStatus(OrderStatus.CANCELLED.name());
                        orderRepo.save(order);
                    } else {
                        throw new BadRequestException(
                                "Inorder to cancel the order, the order should be in pending status");
                    }
                    break;

                default:
                    throw new BadRequestException("Invalid status");

            }
            return ResponseEntity.ok(new SuccessResponse(message(status), order, null));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    private String message(OrderStatus status) {
        switch (status) {
            case PENDING:
                return "Order is pending";
            case SHIPPED:
                return "Order shipped successfully";
            case DELIVERED:
                return "Order delivered successfully";
            case CANCELLED:
                return "Order cancelled successfully";
            default:
                return "Invalid status";
        }
    }

    // get by id
    public ResponseEntity<?> getOrderById(String id) throws BadRequestException, InternalServerException {
        try {

            return ResponseEntity
                    .ok(orderRepo.findOrder(UUID.fromString(id)).orElseThrow(() -> new BadRequestException("Order not found")));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    // my orders
    public ResponseEntity<?> myOrders(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (status != null && startDate != null && endDate != null) {
            return ResponseEntity.ok(orderRepo.filterOrder(email, status.name(), startDate, endDate));
        } else if (status != null) {
            return ResponseEntity.ok(orderRepo.filterOrder(email, status.name()));
        } else if (startDate != null && endDate != null) {
            return ResponseEntity.ok(orderRepo.filterOrder(email, startDate, endDate));
        } else {
            return ResponseEntity.ok(orderRepo.findByUserEmail(email));
        }
    }

}
