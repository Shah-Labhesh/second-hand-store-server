package com.labhesh.secondhandstore.dtos;

import org.springframework.web.multipart.MultipartFile;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {


    @Size(min = 3, message = "Name must be at least 3 characters long")
    private String name;
    @Email(message = "Email must be valid")
    private String email;
    private MultipartFile avatar;
}
