package uy.edu.utec.apifarmayopin.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uy.edu.utec.apifarmayopin.dtos.requests.CartItemRequestDTO;
import uy.edu.utec.apifarmayopin.dtos.responses.CartItemResponseDTO;
import uy.edu.utec.apifarmayopin.dtos.responses.CartResponseDTO;
import uy.edu.utec.apifarmayopin.models.*;
import uy.edu.utec.apifarmayopin.repositories.CartRepository;
import uy.edu.utec.apifarmayopin.repositories.ProductRepository;
import uy.edu.utec.apifarmayopin.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartResponseDTO getCartByUserEmail(String email) {
        Cart cart = getOrCreateCartEntity(email);
        return mapToCartResponseDTO(cart);
    }

    @Transactional
    public CartResponseDTO addItemToCart(String email, CartItemRequestDTO dto) {
        Cart cart = getOrCreateCartEntity(email);
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto con ID: " + dto.getProductId() + " no encontrado"));

        if(product.getStock() < dto.getQuantity()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Stock insuficiente. Disponible: " + product.getStock());
        }
        cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(dto.getProductId()))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity() + dto.getQuantity()),
                        () -> {
                            CartItem newItem = new CartItem();
                            newItem.setCart(cart);
                            newItem.setProduct(product);
                            newItem.setQuantity(dto.getQuantity());
                            cart.getItems().add(newItem);
                        }
                );

        Cart savedCart = cartRepository.save(cart);
        return mapToCartResponseDTO(savedCart);
    }

    @Transactional
    public CartResponseDTO updateItemQuantity(String email, Integer cartItemId, Integer newQuantity) {
        Cart cart = getOrCreateCartEntity(email);

        if (newQuantity <= 0) {
            return removeItemFromCart(email, cartItemId);
        }

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() ->  new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto con ID: " + cartItemId + " no encontrado"));

        if (newQuantity > item.getProduct().getStock()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Stock insuficiente. Disponible: " + item.getProduct().getStock());
        }

        item.setQuantity(newQuantity);

        Cart savedCart = cartRepository.save(cart);
        return mapToCartResponseDTO(savedCart);
    }

    @Transactional
    public CartResponseDTO removeItemFromCart(String email, Integer cartItemId) {
        Cart cart = getOrCreateCartEntity(email);
        cart.getItems().removeIf(item -> item.getId().equals(cartItemId));

        Cart savedCart = cartRepository.save(cart);
        return mapToCartResponseDTO(savedCart);
    }

    private Cart getOrCreateCartEntity(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario con email: " + email + " no encontrado"));

        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    private CartResponseDTO mapToCartResponseDTO(Cart cart) {
        CartResponseDTO cartDTO = new CartResponseDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setTotal(cart.getTotal());

        List<CartItemResponseDTO> itemDTOs = cart.getItems().stream().map(item -> {
            CartItemResponseDTO itemDTO = new CartItemResponseDTO();
            itemDTO.setId(item.getId());
            itemDTO.setProductId(item.getProduct().getId());
            itemDTO.setProductName(item.getProduct().getName());
            itemDTO.setUnitPrice(item.getProduct().getPrice());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setSubtotal(item.getProduct().getPrice() * item.getQuantity());
            return itemDTO;
        }).collect(Collectors.toList());

        cartDTO.setItems(itemDTOs);
        return cartDTO;
    }
}