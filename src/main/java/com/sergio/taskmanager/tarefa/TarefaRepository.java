package com.sergio.taskmanager.tarefa;

import com.sergio.taskmanager.tarefa.enums.PrioridadeTarefa;
import com.sergio.taskmanager.tarefa.enums.StatusTarefa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    List<Tarefa> findByPrioridade(PrioridadeTarefa prioridade);

    List<Tarefa> findByStatus(StatusTarefa status);

}
