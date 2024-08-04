package com.labhesh.secondhandstore.controllers;

import com.labhesh.secondhandstore.dtos.InitiateDto;
import com.labhesh.secondhandstore.dtos.LoginDto;
import com.labhesh.secondhandstore.dtos.RegisterDto;
import com.labhesh.secondhandstore.dtos.ResetPasswordDto;
import com.labhesh.secondhandstore.dtos.VerifyDto;
import com.labhesh.secondhandstore.exception.BadRequestException;
import com.labhesh.secondhandstore.exception.InternalServerException;
import com.labhesh.secondhandstore.service.AuthenticationService;
import com.labhesh.secondhandstore.utils.SuccessResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDto dto)
            throws BadRequestException, InternalServerException {
        return authenticationService.register(dto);
    }

    @PostMapping("/initiate-email-verification")
    public ResponseEntity<?> initEmailVerification(@RequestBody @Valid InitiateDto dto) throws BadRequestException {
        authenticationService.initiateEmailVerification(dto);
        return ResponseEntity.ok(new SuccessResponse("Email verification link sent to your email", null, null));
    }

    @PostMapping("/resend-email-verification")
    public ResponseEntity<?> resendEmailVerification(@RequestBody @Valid InitiateDto dto) throws BadRequestException {
        authenticationService.resendEmailVerification(dto);
        return ResponseEntity.ok(new SuccessResponse("Email verification link sent to your email", null, null));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody @Valid VerifyDto dto) throws BadRequestException {
        return authenticationService.verifyEmail(dto);
    }

    @PostMapping("/initiate-reset-password")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid InitiateDto dto) throws BadRequestException {
        authenticationService.initiatePasswordReset(dto);
        return ResponseEntity.ok(new SuccessResponse("Password reset link sent to your email", null, null));
    }

    @PostMapping("/resend-reset-password")
    public ResponseEntity<?> resendForgotPassword(@RequestBody @Valid InitiateDto dto) throws BadRequestException {
        authenticationService.resendPasswordReset(dto);
        return ResponseEntity.ok(new SuccessResponse("Password reset link sent to your email", null, null));
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
