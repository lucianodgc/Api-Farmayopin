package uy.edu.utec.apifarmayopin.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDTO {
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
}