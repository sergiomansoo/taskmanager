package com.sergio.taskmanager.usuario.dto;

public record UsuarioRequest(
        String nome,
        String email,
        String senha
) {

}
