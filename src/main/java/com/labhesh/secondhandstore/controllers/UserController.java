package com.labhesh.secondhandstore.controllers;

import java.io.IOException;

import com.labhesh.secondhandstore.dtos.ChangePasswordDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.labhesh.secondhandstore.dtos.UpdateUserDto;
import com.labhesh.secondhandstore.exception.BadRequestException;
import com.labhesh.secondhandstore.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "User")
@SecurityRequirement(name = "auth")
public class UserController {
    

    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "UserRole.ALL")
    public ResponseEntity<?> getMe() throws BadRequestException{
        return userService.getMe();
    }

    @PutMapping("/me")
    @Operation(summary = "Update current user", description = "UserRole.ALL")
    public ResponseEntity<?> updateMe(@ModelAttribute @RequestBody @Valid UpdateUserDto dto) throws BadRequestException, IOException {
        return userService.updateMe(dto);
    }

    @PutMapping("/change-password")
    @Operation(summary = "Change the password of current user", description = "UserRole.ALL")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordDto dto) throws BadRequestException {
        return userService.changePassword(dto);
    }

    @DeleteMapping("/me")
    @Operation(summary = "Delete current user", description = "UserRole.ALL")
    public ResponseEntity<?> deleteMe() throws BadRequestException {
        return userService.deleteMe();
    }
}
