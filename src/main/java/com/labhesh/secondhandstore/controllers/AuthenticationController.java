package com.labhesh.secondhandstore.controllers;


import com.labhesh.secondhandstore.dtos.InitiateDto;
import com.labhesh.secondhandstore.dtos.LoginDto;
import com.labhesh.secondhandstore.dtos.RegisterDto;
import com.labhesh.secondhandstore.dtos.ResetPasswordDto;
import com.labhesh.secondhandstore.dtos.VerifyDto;
import com.labhesh.secondhandstore.exception.BadRequestException;
import com.labhesh.secondhandstore.exception.InternalServerException;
import com.labhesh.secondhandstore.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RequiredArgsConstructor
@RestController
@RequestMapping("api/auth")
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto dto) throws BadRequestException {
        return authenticationService.login(dto);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDto dto) throws BadRequestException, InternalServerException {
        return authenticationService.register(dto);
    }

    @PostMapping("/initiate-email-verification")
    public ResponseEntity<?> initEmailVerification(@RequestBody @Valid InitiateDto dto) throws BadRequestException{
        return authenticationService.initiateEmailVerification(dto);
    }

    @PostMapping("/resend-email-verification")
    public ResponseEntity<?> resendEmailVerification(@RequestBody @Valid InitiateDto dto) throws BadRequestException {
        return authenticationService.resendEmailVerification(dto);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody @Valid VerifyDto dto) throws BadRequestException {
        return authenticationService.verifyEmail(dto);
    }

    @PostMapping("/initiate-reset-password")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid InitiateDto dto) throws BadRequestException {
        return authenticationService.initiatePasswordReset(dto);
    }

    @PostMapping("/resend-reset-password")
    public ResponseEntity<?> resendForgotPassword(@RequestBody @Valid InitiateDto dto) throws BadRequestException {
        return authenticationService.resendPasswordReset(dto);
    }

    @PostMapping("/verify-reset-password")
    public ResponseEntity<?> verifyResetPassword(@RequestBody @Valid VerifyDto dto) throws BadRequestException {
        return authenticationService.verifyPasswordReset(dto);
    }

    @PostMapping("/finalize-reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordDto dto) throws BadRequestException {
        return authenticationService.resetPassword(dto);
    }
    
}
