package com.labhesh.secondhandstore.repos;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.labhesh.secondhandstore.models.Cart;
import com.labhesh.secondhandstore.models.Users;



@Repository
public interface CartRepo extends JpaRepository<Cart, UUID>{

    @Query("SELECT c FROM Cart c WHERE c.user = ?1")
    Cart findByUser(Users user);
    
}
