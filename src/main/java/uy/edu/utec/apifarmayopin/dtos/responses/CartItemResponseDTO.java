package uy.edu.utec.apifarmayopin.dtos.responses;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({ "id", "productId", "productName", "quantity", "unitPrice", "subtotal" })
public class CartItemResponseDTO {
    private Integer id;
    private Integer productId;
    private String productName;
    private Double unitPrice;
    private Integer quantity;
    private Double subtotal;
}
