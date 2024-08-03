package com.labhesh.secondhandstore.repos;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.labhesh.secondhandstore.enums.OtpType;
import com.labhesh.secondhandstore.models.Otp;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepo extends JpaRepository<Otp, UUID> {

    Otp findByEmail(String email);

    @Query("SELECT o FROM Otp o WHERE o.email = ?1 AND o.otp = ?2")
    Optional<Otp> findByEmailAndOtp(String email, String otp);
    @Query("SELECT o FROM Otp o WHERE o.email = ?1 AND o.otp = ?2 AND o.otptype = ?3")
    Optional<Otp> findByEmailAndOtpAndType(String email, String otp, OtpType type);
    void deleteByEmail(String email);

    @Query("SELECT o FROM Otp o WHERE o.email = ?1 AND o.otptype = ?2 AND o.isExpired = false")
    Optional<Otp> findByEmailAndOtpType(String email, OtpType emailVerification);

    @Query("SELECT o FROM Otp o WHERE o.isExpired = false")
    List<Otp> NonExpiredOtp();

}
