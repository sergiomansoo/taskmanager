package com.sergio.taskmanager.usuario.dto;

import com.sergio.taskmanager.usuario.enums.Role;

public record UsuarioRequest(
        String nome,
        String email,
        String senha,
        Role role
) {

}
