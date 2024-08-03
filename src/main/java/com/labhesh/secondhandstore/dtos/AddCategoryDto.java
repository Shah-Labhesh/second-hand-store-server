package com.labhesh.secondhandstore.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCategoryDto {

    @NotBlank(message = "Name must not be empty")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z]).{4,}$", message = "Name should contain 1 uppercase and 1 lowercase and be at least 4 characters long")
    private String name;


    @NotNull(message = "Image must not be null")
    private MultipartFile image;
}
