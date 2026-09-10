package uy.edu.utec.apifarmayopin.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uy.edu.utec.apifarmayopin.dtos.requests.AuthRequestDTO;
import uy.edu.utec.apifarmayopin.dtos.requests.UserRequestDTO;
import uy.edu.utec.apifarmayopin.dtos.responses.AuthResponseDTO;
import uy.edu.utec.apifarmayopin.dtos.responses.UserResponseDTO;
import uy.edu.utec.apifarmayopin.services.UserService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO dto) {
        return ResponseEntity.ok(userService.login(dto));
    }
}
