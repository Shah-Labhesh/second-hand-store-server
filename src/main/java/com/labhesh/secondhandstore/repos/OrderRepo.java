package com.labhesh.secondhandstore.repos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.labhesh.secondhandstore.models.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, UUID> {

    @Query("SELECT o FROM Order o WHERE o.id = :id")
    Optional<Order> findOrder(UUID id);

    @Query("SELECT o FROM Order o WHERE o.user.email = :email")
    List<Order> findByUserEmail(String email);

    @Query("SELECT o FROM Order o WHERE o.user.email = :email AND o.status = :status AND o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> filterOrder(String email, String status, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT o FROM Order o WHERE o.user.email = :email AND o.status = :status")
    List<Order> filterOrder(String email, String status);

    @Query("SELECT o FROM Order o WHERE o.user.email = :email AND o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> filterOrder(String email, LocalDateTime startDate, LocalDateTime endDate);
    
}
