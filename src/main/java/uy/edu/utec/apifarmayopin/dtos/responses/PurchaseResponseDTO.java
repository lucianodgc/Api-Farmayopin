package uy.edu.utec.apifarmayopin.dtos.responses;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uy.edu.utec.apifarmayopin.models.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({ "id", "address", "status", "orderDate", "items", "total" })
public class PurchaseResponseDTO {
    private Integer id;
    private LocalDateTime orderDate;
    private Double total;
    private Status status;
    private String address;
    private List<PurchaseItemResponseDTO> items = new ArrayList<>();
}