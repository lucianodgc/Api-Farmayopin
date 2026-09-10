package uy.edu.utec.apifarmayopin.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @Min(1)
    @NotNull
    private Double price;
    @Min(1)
    @NotNull
    private Integer stock;
    private String photo;
    @NotNull
    private Integer categoryId;
}
