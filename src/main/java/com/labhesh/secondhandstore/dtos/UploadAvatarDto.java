package com.labhesh.secondhandstore.dtos;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadAvatarDto {
    
    @NotNull(message = "Avatar is required")
    private MultipartFile avatar;
}
