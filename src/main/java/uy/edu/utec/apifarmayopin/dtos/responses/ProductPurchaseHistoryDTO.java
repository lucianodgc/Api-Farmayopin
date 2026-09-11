package uy.edu.utec.apifarmayopin.dtos.responses;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({ "clientName", "clientEmail", "quantity", "orderDate" })
public class ProductPurchaseHistoryDTO {
    private LocalDateTime orderDate;
    private Integer quantity;
    private String clientEmail;
    private String clientName;
}