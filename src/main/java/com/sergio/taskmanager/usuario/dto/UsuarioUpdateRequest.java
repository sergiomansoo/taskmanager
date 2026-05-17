package com.sergio.taskmanager.usuario.dto;

public record UsuarioUpdateRequest(
        String nome,
        String email
) {
}
