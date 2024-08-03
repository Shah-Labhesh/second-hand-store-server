package com.labhesh.secondhandstore.dtos;

import com.labhesh.secondhandstore.validation.StrongPassword;
import com.labhesh.secondhandstore.validation.ValidOtp;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDto {
    
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "OTP is required")
    @ValidOtp
    private String otp;
    @StrongPassword
    @NotBlank(message = "Password is required")
    private String password;
}
