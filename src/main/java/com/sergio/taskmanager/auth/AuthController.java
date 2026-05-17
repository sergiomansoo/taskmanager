package com.sergio.taskmanager.auth;

import com.sergio.taskmanager.auth.dto.LoginRequestDTO;
import com.sergio.taskmanager.auth.dto.LoginResponseDTO;
import com.sergio.taskmanager.auth.dto.RegisterRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Tag(name = "Auth", description = "Cadastro e autenticação de usuários")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @Operation(summary = "Cadastrar usuário", description = "Cria uma nova conta. O primeiro usuário cadastrado recebe perfil ADMIN.")
    @PostMapping("/register")
    public ResponseEntity<String> registrar(@RequestBody @Valid RegisterRequestDTO registerRequestDTO){
        authService.register(registerRequestDTO);
        return ResponseEntity.ok("Usuario cadastrado com sucesso");
    }
    @Operation(summary = "Login", description = "Autentica o usuário e retorna um token JWT.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
            return ResponseEntity.ok(authService.login(loginRequestDTO));
    }
}
