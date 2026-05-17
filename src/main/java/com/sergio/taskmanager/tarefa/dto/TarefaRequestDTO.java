package com.sergio.taskmanager.tarefa.dto;

import com.sergio.taskmanager.tarefa.enums.PrioridadeTarefa;
import com.sergio.taskmanager.tarefa.enums.StatusTarefa;

import java.time.LocalDate;

public record TarefaRequestDTO(
        String titulo,
        String descricao,
        StatusTarefa status,
        PrioridadeTarefa prioridade,
        LocalDate dataEntrega
) {
}
