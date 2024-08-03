package com.labhesh.secondhandstore.dtos;


import com.labhesh.secondhandstore.validation.StrongPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDto {

    @NotBlank(message = "Old Password must not be empty")
    @StrongPassword(entity = "Old password")
    private String oldPassword;

    @NotBlank(message = "New password must not be empty")
    @StrongPassword(entity = "New Password")
    private String newPassword;

    @NotBlank(message = "Confirm password must not be empty")
    @StrongPassword(entity = "Confirm Password")
    private String confirmPassword;
}
