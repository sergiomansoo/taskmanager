package com.sergio.taskmanager.tarefa.dto;

import com.sergio.taskmanager.tarefa.enums.PrioridadeTarefa;
import com.sergio.taskmanager.tarefa.enums.StatusTarefa;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TarefaResponseDTO(
Long id,
String titulo,
String descricao,
StatusTarefa status,
PrioridadeTarefa prioridadeTarefa,
LocalDateTime dataCricacao,
LocalDate dataEntrega,
LocalDateTime dataConclusao,
Long usuarioId) {
}
