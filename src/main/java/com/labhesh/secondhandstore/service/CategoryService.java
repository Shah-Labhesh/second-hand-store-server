package com.labhesh.secondhandstore.service;


import com.labhesh.secondhandstore.dtos.AddCategoryDto;
import com.labhesh.secondhandstore.dtos.UpdateCategoryDto;
import com.labhesh.secondhandstore.exception.BadRequestException;
import com.labhesh.secondhandstore.exception.InternalServerException;
import com.labhesh.secondhandstore.models.Category;
import com.labhesh.secondhandstore.repos.CategoryRepo;
import com.labhesh.secondhandstore.utils.ImageService;
import com.labhesh.secondhandstore.utils.SuccessResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepo categoryRepo;
    private final ImageService imageService;

    public ResponseEntity<?> addCategory(AddCategoryDto dto) throws InternalServerException {
        try{
            Category category = Category.builder()
                    .name(dto.getName())
                    .file(imageService.saveImage(dto.getImage()))
                    .build();
            categoryRepo.save(category);
            return ResponseEntity.ok(new SuccessResponse("Category added successfully.", category, null));
        }
        catch (Exception e){
            throw new InternalServerException(e.getMessage());
        }
    }


    public ResponseEntity<?> allCategories() throws InternalServerException {
        try{
            return ResponseEntity.ok(categoryRepo.findAllCategories());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public ResponseEntity<?> categoryById(String id) throws BadRequestException, InternalServerException {
        try{
            Category category = categoryRepo.findCategory(UUID.fromString(id)).orElseThrow(() -> new BadRequestException("category not found"));
            return ResponseEntity.ok(category);
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
        catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }


    public ResponseEntity<?> updateCategory(String id, UpdateCategoryDto dto) throws BadRequestException, InternalServerException {
        try{
            Category category = categoryRepo.findCategory(UUID.fromString(id)).orElseThrow(() -> new BadRequestException("category not found"));
            category.setName(dto.getName() != null ? dto.getName() : category.getName());
            category.setFile(dto.getImage() != null ? imageService.saveImage(dto.getImage()) : category.getFile());
            category.setUpdatedDate(LocalDateTime.now());
            categoryRepo.save(category);
            return ResponseEntity.ok(new SuccessResponse("Category updated successfully.", category, null));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
        catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }


    public ResponseEntity<?> deleteCategory(String id) throws BadRequestException, InternalServerException{
        try{
            Category category = categoryRepo.findCategory(UUID.fromString(id)).orElseThrow(() -> new BadRequestException("category not found"));
            category.setDeletedDate(LocalDateTime.now());
            categoryRepo.save(category);
            return ResponseEntity.ok(new SuccessResponse("Category deleted successfully.", null, null));
        } 
        catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
        catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }
}
