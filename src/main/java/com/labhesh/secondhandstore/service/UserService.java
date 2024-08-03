package com.labhesh.secondhandstore.service;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

import com.labhesh.secondhandstore.dtos.ChangePasswordDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.labhesh.secondhandstore.config.PasswordEncoder;
import com.labhesh.secondhandstore.dtos.UpdateUserDto;
import com.labhesh.secondhandstore.exception.BadRequestException;
import com.labhesh.secondhandstore.exception.InternalServerException;
import com.labhesh.secondhandstore.models.Users;
import com.labhesh.secondhandstore.repos.UserRepo;
import com.labhesh.secondhandstore.utils.ImageService;
import com.labhesh.secondhandstore.utils.SuccessResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    public ResponseEntity<?> getMe() throws BadRequestException{
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepo.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<?> updateMe(UpdateUserDto dto) throws BadRequestException, IOException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepo.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
        HashMap<String, Object> map = new HashMap<>();
        map.put("emailUpdate", false);
        user.setName(dto.getName() == null ? user.getName() : dto.getName());
        if (dto.getEmail() != null) {
            if (userRepo.findByEmail(dto.getEmail()).isPresent()) {
                throw new BadRequestException("Email already exists");
            }
            user.setEmail(dto.getEmail());
            user.setVerified(false);
            map.put("emailUpdate", true);
        }
        if (dto.getAvatar() != null) {
            user.setFile(imageService.saveImage(dto.getAvatar()));
        }
        map.put("user", user);
        userRepo.save(user);
        return ResponseEntity.ok(new SuccessResponse("User updated successfully", map, null));
    }

    public ResponseEntity<?> changePassword(ChangePasswordDto dto) throws BadRequestException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepo.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
        if (passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Old password does not match");
        }
        if (dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new BadRequestException("New password and Confirm password does not match");
        }
        user.setPassword(passwordEncoder.encodePassword(dto.getNewPassword()));
        userRepo.save(user);
        return ResponseEntity.ok(new SuccessResponse("Password changed successfully", user, null));
    }

    public ResponseEntity<?> deleteMe() throws BadRequestException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepo.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
        userRepo.delete(user);
        return ResponseEntity.ok(new SuccessResponse("User deleted successfully", null, null));
    }

    public ResponseEntity<?> allUsers(){
        return ResponseEntity.ok(userRepo.findOnlyUsers());
    }

    public ResponseEntity<?> userById(String id) throws BadRequestException, InternalServerException {
        try{
        Users user = userRepo.findById(UUID.fromString(id)).orElseThrow(() -> new BadRequestException("user not found"));
        return ResponseEntity.ok(user);
        }catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
        catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public ResponseEntity<?> deleteUser(String id) throws BadRequestException , InternalServerException{
        try{
        Users user = userRepo.findById(UUID.fromString(id)).orElseThrow(() -> new BadRequestException("user not found"));
        user.setDeletedDate(LocalDateTime.now());
        userRepo.save(user);
        return ResponseEntity.ok(new SuccessResponse("User deleted successfully.", null, null));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
        catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

}
