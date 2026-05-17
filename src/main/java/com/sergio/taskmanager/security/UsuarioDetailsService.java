package com.sergio.taskmanager.security;

import com.sergio.taskmanager.usuario.Usuario;
import com.sergio.taskmanager.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UsuarioDetailsService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;
    @Override
    public @NotNull UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String emailNormalizado = email.trim().toLowerCase();
        if(!usuarioRepository.existsByEmail(emailNormalizado)){
            throw new UsernameNotFoundException("Usuario nao encontrado");
        }
        Usuario usuario=usuarioRepository.findByEmail(emailNormalizado);
        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .roles(usuario.getRole().name())
                .build();

    }
}
