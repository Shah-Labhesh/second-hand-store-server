package com.labhesh.secondhandstore.repos;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.labhesh.secondhandstore.models.Item;

public interface ItemRepo extends JpaRepository<Item, UUID> {

    @Query("SELECT i FROM Item i WHERE i.id = ?1 AND i.deletedDate IS NULL")
    Optional<Item> findItem(UUID id);

    @Query("SELECT i FROM Item i WHERE i.deletedDate IS NULL")
    List<Item> findAllItems();

    @Query("SELECT i FROM Item i WHERE i.name ILIKE %?1% AND i.category.id = ?2 AND i.price >= ?3 AND i.price <= ?4 AND i.deletedDate IS NULL")
    List<Item> searchItems(String name, UUID category, Double minPrice, Double maxPrice);
    
}
