package com.labhesh.secondhandstore.repos;

import com.labhesh.secondhandstore.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepo extends JpaRepository<Category, UUID> {
    @Query("SELECT c FROM Category c WHERE c.id = ?1 AND  c.deletedDate is NULL")
    Optional<Category> findCategory(UUID id);

    @Query("SELECT c FROM Category c WHERE c.deletedDate is NULL")
    List<Category> findAllCategories();
}
