package uy.edu.utec.apifarmayopin.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uy.edu.utec.apifarmayopin.config.JwtUtils;
import uy.edu.utec.apifarmayopin.dtos.requests.AuthRequestDTO;
import uy.edu.utec.apifarmayopin.dtos.requests.UserRequestDTO;
import uy.edu.utec.apifarmayopin.dtos.responses.AuthResponseDTO;
import uy.edu.utec.apifarmayopin.dtos.responses.UserResponseDTO;
import uy.edu.utec.apifarmayopin.models.Role;
import uy.edu.utec.apifarmayopin.models.User;
import uy.edu.utec.apifarmayopin.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Transactional
    public UserResponseDTO register(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado");
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());
        user.setRole(Role.ROLE_CLIENT);

        User savedUser = userRepository.save(user);
        return mapToUserResponseDTO(savedUser);
    }

    public AuthResponseDTO login(AuthRequestDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponseDTO(token, "Bearer", user.getRole().name());
    }

    private UserResponseDTO mapToUserResponseDTO(User user) {
        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole());
        return response;
    }
}
