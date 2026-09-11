package uy.edu.utec.apifarmayopin.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uy.edu.utec.apifarmayopin.dtos.requests.CartItemRequestDTO;
import uy.edu.utec.apifarmayopin.dtos.responses.CartResponseDTO;
import uy.edu.utec.apifarmayopin.services.CartService;

import java.security.Principal;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart(Principal principal) {
        return ResponseEntity.ok(cartService.getCartByUserEmail(principal.getName()));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponseDTO> postItem(
            Principal principal,
            @RequestBody CartItemRequestDTO cartItemRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItemToCart(principal.getName(), cartItemRequestDTO));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartResponseDTO> putQuantity(
            Principal principal,
            @PathVariable Integer itemId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(cartService.updateItemQuantity(principal.getName(), itemId, quantity));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartResponseDTO> deleteItem(
            Principal principal,
            @PathVariable Integer itemId) {
        return ResponseEntity.ok(cartService.removeItemFromCart(principal.getName(), itemId));
    }
}
