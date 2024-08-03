package com.labhesh.secondhandstore.dtos;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCategoryDto {

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z]).{4,}$", message = "Name should contain 1 uppercase and 1 lowercase and be at least 4 characters long")
    private String name;

    private MultipartFile image;
}
