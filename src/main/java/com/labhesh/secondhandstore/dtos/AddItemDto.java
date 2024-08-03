package com.labhesh.secondhandstore.dtos;


import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddItemDto {

    @NotBlank(message = "Name must not be empty")
    @Pattern(regexp = "^[a-zA-Z ]{4,}$", message = "Name must contain only alphabets and at least 4 characters")
    private String name;
    @NotBlank(message = "Description must not be empty")
    @Size(min = 10, message = "Description must be at least 10 characters long")
    private String description;
    @NotBlank(message = "Currency code must not be empty")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must be exactly three uppercase letters")
    private String currencyCode;
    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @DecimalMax(value = "99999999.99", message = "Price must be less than 100 million")
    @Digits(integer = 8, fraction = 2, message = "Price must have up to 8 digits in the integer part and up to 2 digits in the fractional part")
    private Double price = 0.0;
    @NotNull(message = "Image cannot be null")
    private MultipartFile image;
    @NotBlank(message = "Category id must not be empty")
    private String categoryId;
}
