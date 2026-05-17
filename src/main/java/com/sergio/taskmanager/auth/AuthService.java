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
        String email = registerRequestDTO.email().trim().toLowerCase();

            if(usuarioRepository.existsByEmail(email)){
                throw new RuntimeException("Email ja cadastrado");
            }
        Role role = usuarioRepository.count() == 0 ? Role.ADMIN : Role.USER;
        Usuario user= Usuario.builder()
                    .nome(registerRequestDTO.nome())
                    .email(email)
                    .senha(passwordEncoder.encode(registerRequestDTO.senha()))
                    .role(role).build();
            usuarioRepository.save(user);
    }
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO){
        String email = loginRequestDTO.email().trim().toLowerCase();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        loginRequestDTO.senha()
                )
        );
        String token= jwtService.generateToken(email);
        return new LoginResponseDTO(token);
    }
}
