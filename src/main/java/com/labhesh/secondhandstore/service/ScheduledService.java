package com.labhesh.secondhandstore.service;

import java.time.LocalDateTime;

import com.labhesh.secondhandstore.repos.OtpRepo;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ScheduledService {

    private final OtpRepo otpRepo;

    // auto expire otp if expied time is reached or crossed
    public void expireOtp() {
        otpRepo.NonExpiredOtp().forEach(otp -> {
            if (otp.getExpiredDate().isBefore(LocalDateTime.now())) {
                otp.setExpired(true);
                otpRepo.save(otp);
            }
        });
    }
}
