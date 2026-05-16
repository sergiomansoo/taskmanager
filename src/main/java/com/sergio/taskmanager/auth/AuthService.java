package com.sergio.taskmanager.auth;

import com.sergio.taskmanager.auth.dto.LoginRequestDTO;
import com.sergio.taskmanager.auth.dto.LoginResponseDTO;
import com.sergio.taskmanager.auth.dto.RegisterRequestDTO;
import com.sergio.taskmanager.security.JwtService;
import com.sergio.taskmanager.usuario.Usuario;
import com.sergio.taskmanager.usuario.UsuarioRepository;
import com.sergio.taskmanager.usuario.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    public void register(RegisterRequestDTO registerRequestDTO){
            if(usuarioRepository.existsByEmail(registerRequestDTO.email())){
                throw new RuntimeException("Email ja cadastrado");
            }
            Usuario user= Usuario.builder()
                    .nome(registerRequestDTO.nome())
                    .email(registerRequestDTO.email())
                    .senha(passwordEncoder.encode(registerRequestDTO.senha()))
                    .role(Role.USER).build();
            usuarioRepository.save(user);
    }
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.email(),
                        loginRequestDTO.senha()
                )
        );
        String token= jwtService.generateToken(loginRequestDTO.email());
        return new LoginResponseDTO(token);
    }
}
