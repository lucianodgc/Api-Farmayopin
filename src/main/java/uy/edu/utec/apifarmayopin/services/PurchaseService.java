package uy.edu.utec.apifarmayopin.services;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uy.edu.utec.apifarmayopin.dtos.requests.PurchaseRequestDTO;
import uy.edu.utec.apifarmayopin.dtos.responses.ProductPurchaseHistoryDTO;
import uy.edu.utec.apifarmayopin.dtos.responses.PurchaseItemResponseDTO;
import uy.edu.utec.apifarmayopin.dtos.responses.PurchaseResponseDTO;
import uy.edu.utec.apifarmayopin.models.*;
import uy.edu.utec.apifarmayopin.repositories.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PurchaseItemRepository purchaseItemRepository;

    @Transactional
    public PurchaseResponseDTO checkout(String email, PurchaseRequestDTO dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado"));

        if (cart.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El carrito está vacío");
        }

        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setOrderDate(LocalDateTime.now());
        purchase.setStatus(Status.PENDING);
        purchase.setAddress(dto.getAddress());

        List<PurchaseItem> purchaseItems = new ArrayList<>();
        double total = 0.0;

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();

            if (product.getStock() < cartItem.getQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock insuficiente para: " + product.getName());
            }

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            PurchaseItem purchaseItem = new PurchaseItem();
            purchaseItem.setPurchase(purchase);
            purchaseItem.setProduct(product);
            purchaseItem.setQuantity(cartItem.getQuantity());
            purchaseItem.setPrice(product.getPrice());

            purchaseItems.add(purchaseItem);
            total += product.getPrice() * cartItem.getQuantity();
        }

        purchase.setItems(purchaseItems);
        purchase.setTotal(total);

        Purchase savedPurchase = purchaseRepository.save(purchase);

        cart.getItems().clear();
        cartRepository.save(cart);

        return mapToPurchaseResponseDTO(savedPurchase);
    }

    public List<PurchaseResponseDTO> getUserPurchases(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        return purchaseRepository.findByUserOrderByOrderDateDesc(user).stream()
                .map(this::mapToPurchaseResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductPurchaseHistoryDTO> getPurchaseHistoryByProduct(Integer productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto con id: " + productId + " no encontrado");
        }

        return purchaseItemRepository.findByProductIdOrderByPurchaseOrderDateDesc(productId)
                .stream()
                .map(item -> new ProductPurchaseHistoryDTO(
                        item.getPurchase().getOrderDate(),
                        item.getQuantity(),
                        item.getPurchase().getUser().getEmail(),
                        item.getPurchase().getUser().getName()
                ))
                .collect(Collectors.toList());
    }

    private PurchaseResponseDTO mapToPurchaseResponseDTO(Purchase purchase) {
        PurchaseResponseDTO dto = new PurchaseResponseDTO();
        dto.setId(purchase.getId());
        dto.setOrderDate(purchase.getOrderDate());
        dto.setTotal(purchase.getTotal());
        dto.setStatus(purchase.getStatus());
        dto.setAddress(purchase.getAddress());

        List<PurchaseItemResponseDTO> itemDTOs = purchase.getItems().stream().map(item -> {
            PurchaseItemResponseDTO itemDTO = new PurchaseItemResponseDTO();
            itemDTO.setId(item.getId());
            itemDTO.setProductId(item.getProduct().getId());
            itemDTO.setProductName(item.getProduct().getName());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setPrice(item.getPrice());
            itemDTO.setSubtotal(item.getPrice() * item.getQuantity());
            return itemDTO;
        }).collect(Collectors.toList());

        dto.setItems(itemDTOs);
        return dto;
    }
}
