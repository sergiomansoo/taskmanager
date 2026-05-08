package com.sergio.taskmanager.usuario.dto;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        LocalDateTime data_criacao
) {

}
