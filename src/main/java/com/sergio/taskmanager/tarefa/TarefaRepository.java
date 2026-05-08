package com.sergio.taskmanager.tarefa;

import com.sergio.taskmanager.tarefa.enums.PrioridadeTarefa;
import com.sergio.taskmanager.tarefa.enums.StatusTarefa;
import jdk.jshell.Snippet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    List<Tarefa> findByPrioridade(PrioridadeTarefa prioridade);

    List<Tarefa> findByStatus(StatusTarefa status);

    List<Tarefa> findByPrioridadeAndStatus(PrioridadeTarefa prioridade,StatusTarefa status);

    List<Tarefa> findByUsuarioId(Long id);

    List<Tarefa> findByUsuarioIdAndStatus(Long id, StatusTarefa status);
    List<Tarefa> findByUsuarioIdAndPrioridade(Long id,PrioridadeTarefa prioridade);
    List<Tarefa> findByUsuarioIdAndPrioridadeAndStatus(Long id,PrioridadeTarefa prioridade,StatusTarefa status);

    boolean existsByUsuarioId(Long usuarioId);
}
