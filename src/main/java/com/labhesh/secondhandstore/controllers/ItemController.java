package com.labhesh.secondhandstore.controllers;

import com.labhesh.secondhandstore.dtos.ReviewItemDto;
import com.labhesh.secondhandstore.exception.BadRequestException;
import com.labhesh.secondhandstore.exception.InternalServerException;
import com.labhesh.secondhandstore.service.CategoryService;
import com.labhesh.secondhandstore.service.ItemService;
import com.labhesh.secondhandstore.service.ReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
@Tag(name = "Item")
public class ItemController {

    private final CategoryService categoryService;
    private final ItemService itemService;
    private final ReviewService reviewService;

    @Operation(summary = "Get all categories")
    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() throws InternalServerException {
        return categoryService.allCategories();
    }

    @Operation(summary = "Get category by id")
    @GetMapping("/category/{id}")
    public ResponseEntity<?> getCategory(@PathVariable String id) throws BadRequestException, InternalServerException {
        return categoryService.categoryById(id);
    }

    @Operation(summary = "Get all items")
    @GetMapping("/items")
    public ResponseEntity<?> getAllItems() throws InternalServerException {
        return itemService.allItems();
    }

    @Operation(summary = "Get item by id")
    @GetMapping("/item/{id}")
    public ResponseEntity<?> itemById(@PathVariable String id) throws BadRequestException, InternalServerException {
        return itemService.itemById(id);
    }

    @Operation(summary = "Get items by category")
    @GetMapping("/search-item")
    public ResponseEntity<?> searchItem(@RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = true) String category, @RequestParam(required = false, defaultValue = "0.0") Double minPrice,
            @RequestParam(required = false , defaultValue = "99999999.99")  Double maxPrice) throws InternalServerException {
        return itemService.searchItems(name, category, minPrice, maxPrice);
    }

    @SecurityRequirement(name = "auth")
    @Operation(summary = "Add review to item", description = "UserRole.USER")
    @PostMapping("/review")
    public ResponseEntity<?> addReview(@RequestBody ReviewItemDto entity) throws BadRequestException, InternalServerException {
        return reviewService.addReview(entity);
    }

    @Operation(summary = "Get review of item by id")
    @GetMapping("/review/{itemId}")
    public ResponseEntity<?> getReview(@PathVariable String itemId) throws BadRequestException, InternalServerException {
        return reviewService.getReview(itemId);
    }
    

}
