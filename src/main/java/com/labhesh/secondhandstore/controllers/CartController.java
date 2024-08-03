package com.labhesh.secondhandstore.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.labhesh.secondhandstore.dtos.CartItemDto;
import com.labhesh.secondhandstore.dtos.CheckOutCartDto;
import com.labhesh.secondhandstore.dtos.UpdateCartItemDto;
import com.labhesh.secondhandstore.exception.BadRequestException;
import com.labhesh.secondhandstore.exception.InternalServerException;
import com.labhesh.secondhandstore.service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/cart")
@SecurityRequirement(name = "auth")
@Tag(name = "Cart")
@RequiredArgsConstructor
public class CartController {
    

    private final CartService cartService;

    @Operation(summary = "Get my cart", description = "UserRole.USER")
    @GetMapping
    public ResponseEntity<?> myCart() throws BadRequestException {
        return cartService.myCart();
    }

    @Operation(summary = "Add item to cart", description = "UserRole.USER")
    @PostMapping
    public ResponseEntity<?> addItemToCart(@RequestBody @Valid CartItemDto dto) throws BadRequestException, InternalServerException {
        return cartService.addItemToCart(dto);
    }

    @Operation(summary = "Update item in cart", description = "UserRole.USER")
    @PutMapping("/item")
    public ResponseEntity<?> updateItemInCart(@RequestBody @Valid UpdateCartItemDto dto) throws BadRequestException, InternalServerException {
        return cartService.updateCartItem(dto);
    }

    @Operation(summary = "Remove item from cart", description = "UserRole.USER")
    @DeleteMapping("/item/{id}")
    public ResponseEntity<?> removeItemFromCart(@PathVariable String id) throws BadRequestException, InternalServerException {
        return cartService.removeCartItem(id);
    }

    @Operation(summary = "Clear cart", description = "UserRole.USER")
    @DeleteMapping
    public ResponseEntity<?> clearCart() throws BadRequestException {
        return cartService.clearCart();
    }

    @Operation(summary = "Check out cart", description = "UserRole.USER")
    @PostMapping("/checkout")
    public ResponseEntity<?> checkOutCart(@RequestBody @Valid CheckOutCartDto dto) throws BadRequestException {
        return cartService.checkoutCart(dto);
    }
    
}
