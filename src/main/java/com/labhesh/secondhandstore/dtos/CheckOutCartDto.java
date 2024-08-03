package com.labhesh.secondhandstore.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckOutCartDto {
    
    @NotBlank(message = "Shipping address must not be empty")
    private String shippingAddress;
}
