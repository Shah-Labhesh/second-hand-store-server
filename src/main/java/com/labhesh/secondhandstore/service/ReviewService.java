package com.labhesh.secondhandstore.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.labhesh.secondhandstore.dtos.ReviewItemDto;
import com.labhesh.secondhandstore.exception.BadRequestException;
import com.labhesh.secondhandstore.exception.InternalServerException;
import com.labhesh.secondhandstore.models.Item;
import com.labhesh.secondhandstore.models.Review;
import com.labhesh.secondhandstore.models.Users;
import com.labhesh.secondhandstore.repos.ItemRepo;
import com.labhesh.secondhandstore.repos.ReviewRepo;
import com.labhesh.secondhandstore.repos.UserRepo;
import com.labhesh.secondhandstore.utils.SuccessResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ItemRepo itemRepo;
    private final ReviewRepo reviewRepo;
    private final UserRepo userRepo;

    // add review to item
    public ResponseEntity<?> addReview(ReviewItemDto dto) throws BadRequestException, InternalServerException {
        try {
            Item item = itemRepo.findItem(UUID.fromString(dto.getItemId()))
                    .orElseThrow(() -> new BadRequestException("Item not found"));
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Users user = userRepo.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
            Review review = reviewRepo.findByUserAndItem(user, item);
            if (review != null) {
                review.setRating(dto.getRating());
                review.setComment(dto.getComment());
                review.setReviewDate(LocalDateTime.now());
                reviewRepo.save(review);
            } else {
                review = Review.builder()
                        .item(item)
                        .user(user)
                        .rating(dto.getRating())
                        .comment(dto.getComment())
                        .reviewDate(LocalDateTime.now())
                        .build();
                reviewRepo.save(review);
            }
            return ResponseEntity.ok(new SuccessResponse("Review added successfully", review, null));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }

    }

    // get review of item by id
    public ResponseEntity<?> getReview(String itemId) throws BadRequestException, InternalServerException {
        try{
        Item item = itemRepo.findItem(UUID.fromString(itemId)).orElseThrow(() -> new BadRequestException("Item not found"));
        return ResponseEntity.ok(new SuccessResponse("Review fetched successfully", reviewRepo.findByItem(item), null));
        }catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
        catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }
}
