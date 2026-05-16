package com.sergio.taskmanager.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank(message = "Informe um nome")
        String nome,
        @NotBlank(message="Informe um email")
        @Email(message = "Email invalido")
        String email,
        @NotBlank(message = "Informe uma senha")
        @Size(min=7, message = "Senha deve possuir mais de 6 caracteres")
        String senha
) {
}
