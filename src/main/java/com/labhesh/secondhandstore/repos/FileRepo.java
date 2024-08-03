package com.labhesh.secondhandstore.repos;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.labhesh.secondhandstore.models.Files;

@Repository
public interface FileRepo extends JpaRepository<Files, UUID> {

    @Query("SELECT f FROM Files f WHERE f.filename = ?1")
    Optional<Files> findByFilename(String filename);
}
