package com.labhesh.secondhandstore.dtos;

import com.labhesh.secondhandstore.validation.StrongPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotBlank(message = "Email must not be empty")
    @Email(message = "Email must be valid")
    private String email;
    @NotBlank(message = "Password must not be empty")
    @StrongPassword
    private String password;
}
