package com.labhesh.secondhandstore.dtos;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    
    @NotBlank(message = "Item id is required")
    private String itemId;
    @NotNull(message = "Quantity is required")
    @Digits(integer = 3, fraction = 0, message = "Quantity must be a number with up to 3 digits")
    private int quantity;
}
