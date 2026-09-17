package uy.edu.utec.apifarmayopin.dtos.responses;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({ "id", "categoryId", "categoryName", "name", "description", "price", "stock", "photo" })
public class ProductResponseDTO {
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String photo;
    private Integer categoryId;
    private String categoryName;
}
