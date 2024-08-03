package com.labhesh.secondhandstore.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitiateDto {
    @NotBlank(message = "Email must not be empty")
    @Email(message = "Email must be valid")
    private String email;    
}
