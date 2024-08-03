package com.labhesh.secondhandstore.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.labhesh.secondhandstore.dtos.UploadAvatarDto;
import com.labhesh.secondhandstore.exception.BadRequestException;
import com.labhesh.secondhandstore.exception.InternalServerException;
import com.labhesh.secondhandstore.service.AuthenticationService;
import com.labhesh.secondhandstore.utils.ImageService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
@Tag(name = "Image")
public class ImageController {
    

    private final ImageService imageService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Upload avatar of the user with user_id")
    @PostMapping("/user/{id}")
    public ResponseEntity<?> uploadImage(@PathVariable String id, @ModelAttribute @RequestBody @Valid UploadAvatarDto dto) throws  BadRequestException, InternalServerException {
        return authenticationService.uploadAvatar(id, dto);
    }

    @Operation(summary = "Get image by filename")
    @GetMapping("/{filename}")
    public ResponseEntity<?> getImage(@PathVariable String filename) throws BadRequestException {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageService.getImage(filename));
    }
    
}
