package com.labhesh.secondhandstore.dtos;


import com.labhesh.secondhandstore.enums.UserRole;
import com.labhesh.secondhandstore.validation.EnumValidator;
import com.labhesh.secondhandstore.validation.StrongPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

    @NotBlank(message = "Name must not be empty")
    @Size(min = 3, message = "Name must be at least 3 characters long")
    private String name;
    @NotBlank(message = "Email must not be empty")
    @Email(message = "Email must be valid")
    private String email;
    @NotBlank(message = "Password must not be empty")
    @StrongPassword
    private String password;
    @EnumValidator(enumClass = UserRole.class, message = "Invalid role. Must be one of ADMIN, USER")
    private String role;

}
