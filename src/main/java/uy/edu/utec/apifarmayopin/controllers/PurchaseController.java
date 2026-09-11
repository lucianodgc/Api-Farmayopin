package uy.edu.utec.apifarmayopin.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uy.edu.utec.apifarmayopin.dtos.requests.PurchaseRequestDTO;
import uy.edu.utec.apifarmayopin.dtos.responses.ProductPurchaseHistoryDTO;
import uy.edu.utec.apifarmayopin.dtos.responses.PurchaseResponseDTO;
import uy.edu.utec.apifarmayopin.services.PurchaseService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping("/checkout")
    public ResponseEntity<PurchaseResponseDTO> checkout(
            Principal principal,
            @RequestBody PurchaseRequestDTO purchaseRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(purchaseService.checkout(principal.getName(), purchaseRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<PurchaseResponseDTO>> getUserPurchases(Principal principal) {
        return ResponseEntity.ok(purchaseService.getUserPurchases(principal.getName()));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductPurchaseHistoryDTO>> getProductHistory(@PathVariable Integer productId) {
        return ResponseEntity.ok(purchaseService.getPurchaseHistoryByProduct(productId));
    }
}