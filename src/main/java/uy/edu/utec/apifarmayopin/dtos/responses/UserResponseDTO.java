package uy.edu.utec.apifarmayopin.dtos.responses;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uy.edu.utec.apifarmayopin.models.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({ "id", "email", "name", "phone", "role" })
public class UserResponseDTO {
    private Long id;
    private String email;
    private String name;
    private String phone;
    private Role role;
}
