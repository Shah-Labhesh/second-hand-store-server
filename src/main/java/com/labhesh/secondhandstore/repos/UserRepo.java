package com.labhesh.secondhandstore.repos;

import com.labhesh.secondhandstore.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<Users, UUID> {


    Optional<Users> findByEmail(String username);

    @Query("SELECT u FROM Users u WHERE u.role = com.labhesh.secondhandstore.enums.UserRole.USER")
    List<Users> findOnlyUsers();
}
