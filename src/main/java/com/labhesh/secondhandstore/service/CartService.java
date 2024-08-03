package com.labhesh.secondhandstore.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.labhesh.secondhandstore.dtos.CartItemDto;
import com.labhesh.secondhandstore.dtos.CheckOutCartDto;
import com.labhesh.secondhandstore.dtos.UpdateCartItemDto;
import com.labhesh.secondhandstore.exception.BadRequestException;
import com.labhesh.secondhandstore.exception.InternalServerException;
import com.labhesh.secondhandstore.models.Cart;
import com.labhesh.secondhandstore.models.CartItem;
import com.labhesh.secondhandstore.models.Item;
import com.labhesh.secondhandstore.models.Order;
import com.labhesh.secondhandstore.models.OrderItem;
import com.labhesh.secondhandstore.models.Users;
import com.labhesh.secondhandstore.repos.CartItemsRepo;
import com.labhesh.secondhandstore.repos.CartRepo;
import com.labhesh.secondhandstore.repos.ItemRepo;
import com.labhesh.secondhandstore.repos.OrderRepo;
import com.labhesh.secondhandstore.repos.UserRepo;
import com.labhesh.secondhandstore.utils.SuccessResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepo cartRepo;
    private final UserRepo userRepo;
    private final CartItemsRepo cartItemsRepo;
    private final ItemRepo itemRepo;
    private final OrderRepo orderRepo;

    public ResponseEntity<?> myCart() throws BadRequestException {
        return ResponseEntity.ok(cart());

    }

    private Cart cart() throws BadRequestException {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepo.findByEmail(name).orElseThrow(() -> new BadRequestException("User not found"));
        Cart cart = cartRepo.findByUser(user);
        if (cart == null) {
            cart = Cart.builder()
                    .user(user)
                    .build();
            cartRepo.save(cart);
        }
        return cart;
    }

    // add Item to cart
    public ResponseEntity<?> addItemToCart(CartItemDto dto) throws BadRequestException, InternalServerException {
        try {
            Cart userCart = cart();
        Item item = itemRepo.findItem(UUID.fromString(dto.getItemId()))
                .orElseThrow(() -> new BadRequestException("Item not found or has been deleted"));
        List<CartItem> cartItems = userCart.getCartItems();
        cartItems.forEach(cartItem -> {
            if (cartItem.getItem().getId().equals(item.getId())) {
                cartItem.setQuantity(cartItem.getQuantity() + dto.getQuantity());
                cartItemsRepo.save(cartItem);
                return;
            } else {
                CartItem newItem = CartItem.builder()
                        .cart(userCart)
                        .item(item)
                        .quantity(dto.getQuantity())
                        .build();
                cartItemsRepo.save(newItem);
                cartItems.add(newItem);
            }
        });
        userCart.setCartItems(cartItems);
        cartRepo.save(userCart);
        return ResponseEntity.ok(new SuccessResponse("Item added to cart successfully", userCart, null));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
        catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    // update cart item
    public ResponseEntity<?> updateCartItem(UpdateCartItemDto dto) throws BadRequestException, InternalServerException {
        try {
            Cart userCart = cart();
        Item item = itemRepo.findItem(UUID.fromString(dto.getItemId()))
                .orElseThrow(() -> new BadRequestException("Item not found or has been deleted"));
        List<CartItem> cartItems = userCart.getCartItems();
        cartItems.forEach(cartItem -> {
            if (cartItem.getItem().getId().equals(item.getId())) {
                switch (dto.getOperation()) {
                    case "INCREASE":
                        cartItem.setQuantity(cartItem.getQuantity() + dto.getQuantity());
                        break;
                    default:
                        cartItem.setQuantity(cartItem.getQuantity() - dto.getQuantity());
                        break;
                }
                cartItemsRepo.save(cartItem);
                return;
            }
        });
        userCart.setCartItems(cartItems);
        cartRepo.save(userCart);
        return ResponseEntity.ok(new SuccessResponse("Item updated in cart successfully", userCart, null));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            // TODO: handle exception
            throw new InternalServerException(e.getMessage());
        }
    }

    // remove cart item
    public ResponseEntity<?> removeCartItem(String itemId) throws BadRequestException, InternalServerException {
        try {
            Cart userCart = cart();
        Item item = itemRepo.findItem(UUID.fromString(itemId))
                .orElseThrow(() -> new BadRequestException("Item not found or has been deleted"));
        List<CartItem> cartItems = userCart.getCartItems();
        cartItems.forEach(cartItem -> {
            if (cartItem.getItem().getId().equals(item.getId())) {
                cartItemsRepo.delete(cartItem);
                cartItems.remove(cartItem);
                userCart.setCartItems(cartItems);
                cartRepo.save(userCart);
                return;
            }
        });
        return ResponseEntity.ok(new SuccessResponse("Item removed from cart successfully", userCart, null));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            // TODO: handle exception
            throw new InternalServerException(e.getMessage());
        }
    }

    public ResponseEntity<?> clearCart() throws BadRequestException {
        Cart userCart = cart();
        List<CartItem> cartItems = userCart.getCartItems();
        cartItemsRepo.deleteAll(cartItems);
        userCart.setCartItems(null);
        cartRepo.save(userCart);
        return ResponseEntity.ok(new SuccessResponse("Cart cleared successfully", userCart, null));
    }

    // check out cart
    public ResponseEntity<?> checkoutCart(CheckOutCartDto dto) throws BadRequestException {
        Cart userCart = cart();
        List<CartItem> cartItems = userCart.getCartItems();
        cartItemsRepo.deleteAll(cartItems);
        userCart.setCartItems(null);
        cartRepo.save(userCart);

        Order order = Order.builder()
                .orderDate(LocalDateTime.now())
                .shippingAddress(dto.getShippingAddress())
                .user(userCart.getUser())
                .build();
        List<OrderItem> orderItems = castToOrderItems(cartItems);
        orderItems.forEach(
                item -> {
                    item.setOrder(order);
                });
        order.setOrderItems(orderItems);
        order.setShippingCost(100.0);
        order.setTax(calculateTax(orderItems));
        order.setTotalAmount(
                orderItems.stream().mapToDouble(OrderItem::getPrice).sum() + order.getShippingCost() + order.getTax());
        orderRepo.save(order);
        return ResponseEntity.ok(new SuccessResponse("Cart checkout successfully", order, null));
    }

    private List<OrderItem> castToOrderItems(List<CartItem> items) {
        return items.stream()
                .map(cartItem -> OrderItem.builder()
                        .item(cartItem.getItem())
                        .quantity(cartItem.getQuantity())
                        .price(cartItem.getItem().getPrice() * cartItem.getQuantity())
                        .build())
                .toList();
    }

    private Double calculateTax(List<OrderItem> items) {
        double totalAmount = items.stream()
                .mapToDouble(OrderItem::getPrice)
                .sum();
        return totalAmount * 0.13;
    }

}
