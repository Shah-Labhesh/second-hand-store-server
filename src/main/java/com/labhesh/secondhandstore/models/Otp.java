package com.labhesh.secondhandstore.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.labhesh.secondhandstore.enums.OtpType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "otps")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Otp {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String email;
    private String otp;
    @Enumerated(EnumType.STRING)
    private OtpType otptype;
    @Builder.Default
    private boolean isExpired = false;
    @Builder.Default
    private LocalDateTime createdDate = LocalDateTime.now();
    @Builder.Default
    private LocalDateTime expiredDate = LocalDateTime.now().plusMinutes(10);

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Users user;

}
