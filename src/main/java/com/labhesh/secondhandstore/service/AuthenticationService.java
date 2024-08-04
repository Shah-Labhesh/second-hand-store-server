package com.labhesh.secondhandstore.service;

import com.labhesh.secondhandstore.config.JwtTokenUtil;
import com.labhesh.secondhandstore.config.PasswordEncoder;
import com.labhesh.secondhandstore.dtos.InitiateDto;
import com.labhesh.secondhandstore.dtos.LoginDto;
import com.labhesh.secondhandstore.dtos.RegisterDto;
import com.labhesh.secondhandstore.dtos.ResetPasswordDto;
import com.labhesh.secondhandstore.dtos.UploadAvatarDto;
import com.labhesh.secondhandstore.dtos.VerifyDto;
import com.labhesh.secondhandstore.enums.OtpType;
import com.labhesh.secondhandstore.enums.UserRole;
import com.labhesh.secondhandstore.exception.BadRequestException;
import com.labhesh.secondhandstore.exception.InternalServerException;
import com.labhesh.secondhandstore.models.Files;
import com.labhesh.secondhandstore.models.Otp;
import com.labhesh.secondhandstore.models.Users;
import com.labhesh.secondhandstore.repos.OtpRepo;
import com.labhesh.secondhandstore.repos.UserRepo;
import com.labhesh.secondhandstore.utils.ImageService;
import com.labhesh.secondhandstore.utils.OtpUtils;
import com.labhesh.secondhandstore.utils.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@EnableAsync
public class AuthenticationService {

    private final UserRepo userRepo;
    private final OtpRepo otpRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final MailService mailService;
    private final ImageService imageService;

    public UserDetails loadUserByUsername(String username) {
        Users user = userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User don't exist"));
        if (!user.isVerified()) {
            throw new UsernameNotFoundException("User is not verified");
        }
        return user;
    }

    public ResponseEntity<?> login(LoginDto dto) throws BadRequestException {
        Users user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadRequestException("User don't exist"));
        if (passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid credential");
        }
        if (!user.isVerified()) {
            throw new BadRequestException("User is not verified");
        }
        String token = jwtTokenUtil.generateToken(user);
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("message", "User logged in successfully");
        map.put("user", user);
        return ResponseEntity.ok(map);
    }

    public ResponseEntity<?> register(RegisterDto dto) throws BadRequestException, InternalServerException {
        try {
            Users user = Users.builder()
                    .email(dto.getEmail())
                    .name(dto.getName())
                    .password(passwordEncoder.encodePassword(dto.getPassword()))
                    .role(getUserRole(dto.getRole()))
                    .build();
            userRepo.save(user);
            return ResponseEntity.created(null).body(new SuccessResponse("User registered successfully", user, null));
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Email address already in use");
        } catch (Exception ex) {
            throw new InternalServerException(ex.getMessage());
        }
    }

    @Async
    public void initiateEmailVerification(InitiateDto dto) throws BadRequestException {
        Users user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadRequestException("User don't exist"));
        if (user.isVerified()) {
            throw new BadRequestException("User is already verified");
        }
        Otp otp = Otp.builder()
                .email(dto.getEmail())
                .otp(OtpUtils.generateOtp())
                .otptype(OtpType.EMAIL_VERIFICATION)
                .user(user)
                .build();
        otpRepo.save(otp);
        // send email verification link
        mailService.sendOtpEmail(user.getEmail(), user.getName(), otp.getOtp(), OtpType.EMAIL_VERIFICATION);
    }

    // resend email verification
    @Async
    public void resendEmailVerification(InitiateDto dto) throws BadRequestException {
        Users user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadRequestException("User don't exist"));
        if (user.isVerified()) {
            throw new BadRequestException("User is already verified");
        }
        Otp otp = otpRepo.findByEmailAndOtpType(dto.getEmail(), OtpType.EMAIL_VERIFICATION)
                .orElseThrow(() -> new BadRequestException("OTP not found"));
        otp.setOtp(OtpUtils.generateOtp());
        otp.setExpired(false);
        otp.setExpiredDate(otp.getCreatedDate().plusMinutes(10));
        otpRepo.save(otp);
        // send email verification link
        mailService.sendOtpEmail(user.getEmail(), user.getName(), otp.getOtp(), OtpType.EMAIL_VERIFICATION);
    }

    // Verify email
    public ResponseEntity<?> verifyEmail(VerifyDto dto) throws BadRequestException {
        Users user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadRequestException("User don't exist"));
        if (user.isVerified()) {
            throw new BadRequestException("User is already verified");
        }
        System.out.println(dto.getOtp());
        Otp otp = otpRepo.findByEmailAndOtpAndType(dto.getEmail(), dto.getOtp(), OtpType.EMAIL_VERIFICATION)
                .orElseThrow(() -> new BadRequestException("Invalid OTP"));
        if (otp.isExpired()) {
            throw new BadRequestException("OTP is expired");
        }
        user.setVerified(true);
        userRepo.save(user);
        return ResponseEntity.ok(new SuccessResponse("User verified successfully", null, null));
    }

    // initiate password reset
    @Async
    public void initiatePasswordReset(InitiateDto dto) throws BadRequestException {
        Users user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadRequestException("User don't exist"));
        Otp otp = Otp.builder()
                .email(dto.getEmail())
                .otp(OtpUtils.generateOtp())
                .otptype(OtpType.PASSWORD_RESET)
                .user(user)
                .build();
        otpRepo.save(otp);
        // send email verification link
        mailService.sendOtpEmail(user.getEmail(), user.getName(), otp.getOtp(), OtpType.PASSWORD_RESET);
    }

    // resend password reset
    @Async
    public void resendPasswordReset(InitiateDto dto) throws BadRequestException {
        Users user = userRepo.findByEmail(dto.getEmail()).orElseThrow(() -> new BadRequestException("User don't exist"));
        Otp otp = otpRepo.findByEmailAndOtpType(dto.getEmail(), OtpType.PASSWORD_RESET)
                .orElseThrow(() -> new BadRequestException("OTP not found"));
        otp.setOtp(OtpUtils.generateOtp());
        otp.setExpired(false);
        otp.setExpiredDate(otp.getCreatedDate().plusMinutes(10));
        otpRepo.save(otp);
        // send email verification link
        mailService.sendOtpEmail(user.getEmail(), user.getName(), otp.getOtp(), OtpType.PASSWORD_RESET);
    }

    // verify password reset
    public ResponseEntity<?> verifyPasswordReset(VerifyDto dto) throws BadRequestException {
        userRepo.findByEmail(dto.getEmail()).orElseThrow(() -> new BadRequestException("User don't exist"));
        Otp otp = otpRepo.findByEmailAndOtpAndType(dto.getEmail(), dto.getOtp(), OtpType.PASSWORD_RESET)
                .orElseThrow(() -> new BadRequestException("Invalid OTP"));
        if (otp.isExpired()) {
            throw new BadRequestException("OTP is expired");
        }
        otp.setExpired(true);
        otpRepo.save(otp);
        return ResponseEntity.ok(new SuccessResponse("Reset password verified", null, null));
    }



    // reset password
    public ResponseEntity<?> resetPassword(ResetPasswordDto dto) throws BadRequestException {
        Users user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadRequestException("User don't exist"));
        otpRepo.findByEmailAndOtpAndType(dto.getEmail(), dto.getOtp(), OtpType.PASSWORD_RESET)
                .orElseThrow(() -> new BadRequestException("Invalid OTP"));
        user.setPassword(passwordEncoder.encodePassword(dto.getPassword()));
        userRepo.save(user);
        return ResponseEntity.ok(new SuccessResponse("Password reset successfully", null, null));
    }

    // upload avatar 
    public ResponseEntity<?> uploadAvatar( String id, UploadAvatarDto dto) throws BadRequestException, InternalServerException {
        try {
            Users user = userRepo.findById(UUID.fromString(id)).orElseThrow(() -> new BadRequestException("User not found"));
        Files file = imageService.saveImage(dto.getAvatar());
        user.setFile(file);
        userRepo.save(user);
        return ResponseEntity.ok(new SuccessResponse("Avatar uploaded successfully", user, null));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
        catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    private UserRole getUserRole(String role) {
        return switch (role) {
            case "ADMIN" -> UserRole.ADMIN;
            default -> UserRole.USER;
        };
    }

}
