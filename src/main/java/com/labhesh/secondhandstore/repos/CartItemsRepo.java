package com.labhesh.secondhandstore.repos;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.labhesh.secondhandstore.models.CartItem;

@Repository
public interface CartItemsRepo extends JpaRepository<CartItem, UUID> {
    
}
