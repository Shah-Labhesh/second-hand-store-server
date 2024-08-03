package com.labhesh.secondhandstore.controllers;


import com.labhesh.secondhandstore.dtos.AddCategoryDto;
import com.labhesh.secondhandstore.dtos.AddItemDto;
import com.labhesh.secondhandstore.dtos.UpdateCategoryDto;
import com.labhesh.secondhandstore.dtos.UpdateItemDto;
import com.labhesh.secondhandstore.enums.OrderStatus;
import com.labhesh.secondhandstore.exception.BadRequestException;
import com.labhesh.secondhandstore.exception.InternalServerException;
import com.labhesh.secondhandstore.service.CategoryService;
import com.labhesh.secondhandstore.service.ItemService;
import com.labhesh.secondhandstore.service.OrderService;
import com.labhesh.secondhandstore.service.ReviewService;
import com.labhesh.secondhandstore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = "auth")
@Tag(name = "Admin")
public class AdminController {

    private final UserService userService;
    private final CategoryService categoryService;
    private final ItemService itemService;
    private final OrderService orderService;
    private final ReviewService reviewService;

    @Operation(summary = "Get all users", description = "UserRole.ADMIN")
    @GetMapping("/users")
    public ResponseEntity<?> allUsers(){
        return userService.allUsers();
    }

    @Operation(summary = "Get user by id", description = "UserRole.ADMIN")
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id) throws BadRequestException, InternalServerException {
        return userService.userById(id);
    }

    @Operation(summary = "Delete user by id", description = "UserRole.ADMIN")
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) throws BadRequestException, InternalServerException {
        return userService.deleteUser(id);
    }

    @Operation(summary = "Add new category", description = "UserRole.ADMIN")
    @PostMapping("/category")
    public ResponseEntity<?> addCategory(@ModelAttribute @RequestBody @Valid AddCategoryDto category) throws InternalServerException {
        return categoryService.addCategory(category);
    }

    @Operation(summary = "Get all categories", description = "UserRole.ADMIN")
    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() throws InternalServerException {
        return categoryService.allCategories();
    }

    @Operation(summary = "Get category by id", description = "UserRole.ADMIN")
    @GetMapping("/category/{id}")
    public ResponseEntity<?> getCategory(@PathVariable String id) throws BadRequestException, InternalServerException {
        return categoryService.categoryById(id);
    }

    @Operation(summary = "Update category by id", description = "UserRole.ADMIN")
    @PutMapping("/category/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable String id, @ModelAttribute @RequestBody @Valid UpdateCategoryDto category) throws BadRequestException, InternalServerException {
        return categoryService.updateCategory(id,category);
    }

    @Operation(summary = "Delete category by id", description = "UserRole.ADMIN")
    @DeleteMapping("/category/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable String id) throws BadRequestException, InternalServerException {
        return categoryService.deleteCategory(id);
    }

    @Operation(summary = "Add new item", description = "UserRole.ADMIN")
    @PostMapping("/item")
    public ResponseEntity<?> addItem(@ModelAttribute @RequestBody @Valid AddItemDto item) throws InternalServerException, BadRequestException {
        return itemService.addItem(item);
    }

    @Operation(summary = "Get all items", description = "UserRole.ADMIN")
    @GetMapping("/items")
    public ResponseEntity<?> getAllItems() throws InternalServerException {
        return itemService.allItems();
    }

    @Operation(summary = "Get item by id", description = "UserRole.ADMIN")
    @GetMapping("/item/{id}")
    public ResponseEntity<?> itemById(@PathVariable String id) throws BadRequestException, InternalServerException {
        return itemService.itemById(id);
    }

    @Operation(summary = "Update item by id", description = "UserRole.ADMIN")
    @PutMapping("/item/{id}")
    public ResponseEntity<?> updateItem(@PathVariable String id, @ModelAttribute @RequestBody @Valid UpdateItemDto item) throws BadRequestException, InternalServerException, IOException {
        return itemService.updateItem(id,item);
    }

    @Operation(summary = "Delete item by id", description = "UserRole.ADMIN")
    @DeleteMapping("/item/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable String id) throws BadRequestException, InternalServerException {
        return itemService.deleteItem(id);
    }


    @Operation(summary = "Get all orders", description = "UserRole.ADMIN")
    @GetMapping("/orders")
    public ResponseEntity<?> allOrders(){
        return orderService.allOrders();
    }

    @Operation(summary = "Change order status", description = "UserRole.ADMIN")
    @PutMapping("/order/{id}")
    public ResponseEntity<?> changeOrderStatus(@PathVariable String id, @RequestParam OrderStatus status) throws BadRequestException, InternalServerException {
        return orderService.changeOrderStatus(id, status);
    }

    @Operation(summary = "Get order by id", description = "UserRole.ADMIN")
    @GetMapping("/order/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id) throws BadRequestException, InternalServerException {
        return orderService.getOrderById(id);
    }

    @Operation(summary = "Get review of item by id")
    @GetMapping("/review/{itemId}")
    public ResponseEntity<?> getReview(@PathVariable String itemId) throws BadRequestException, InternalServerException {
        return reviewService.getReview(itemId);
    }





}
