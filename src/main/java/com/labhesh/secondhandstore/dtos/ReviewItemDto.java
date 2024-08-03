package com.labhesh.secondhandstore.dtos;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewItemDto {

    @NotBlank(message = "Item id is required")
    private String itemId;
    @NotNull(message = "Rating is required")
    // 1 to 5 rating
    @Digits(integer = 1, fraction = 0, message = "Rating should be between 1 to 5")
    private int rating;
    @NotBlank(message = "Comment is required")
    @Size(min = 10, max = 100, message = "Comment should be between 10 to 100 characters")
    private String comment;
    
}
