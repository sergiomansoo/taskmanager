package com.sergio.taskmanager.auth;

import com.sergio.taskmanager.auth.dto.LoginRequestDTO;
import com.sergio.taskmanager.auth.dto.LoginResponseDTO;
import com.sergio.taskmanager.auth.dto.RegisterRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<String> registrar(@RequestBody @Valid RegisterRequestDTO registerRequestDTO){
        authService.register(registerRequestDTO);
        return ResponseEntity.ok("Usuario cadastrado com sucesso");
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
            return ResponseEntity.ok(authService.login(loginRequestDTO));
    }
}
