package uy.edu.utec.apifarmayopin.dtos.responses;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({ "id", "productId", "productName", "quantity", "price", "subtotal" })
public class PurchaseItemResponseDTO {
    private Integer id;
    private Integer productId;
    private String productName;
    private Integer quantity;
    private Double price;
    private Double subtotal;
}
