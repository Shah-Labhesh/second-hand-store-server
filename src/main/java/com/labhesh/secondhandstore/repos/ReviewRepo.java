package com.labhesh.secondhandstore.repos;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.labhesh.secondhandstore.models.*;
import com.labhesh.secondhandstore.models.Review;

@Repository
public interface ReviewRepo extends JpaRepository<Review, UUID> {

    @Query("SELECT r FROM Review r WHERE r.user = ?1 AND r.item = ?2")
    Review findByUserAndItem(Users user, Item item);

    @Query("SELECT r FROM Review r WHERE r.item = ?1")
    Review findByItem(Item item);

}