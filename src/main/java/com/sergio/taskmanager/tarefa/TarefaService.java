package com.sergio.taskmanager.tarefa;

import com.sergio.taskmanager.tarefa.dto.TarefaRequestDTO;
import com.sergio.taskmanager.tarefa.dto.TarefaResponseDTO;
featimport com.sergio.taskmanager.tarefa.enums.PrioridadeTarefa;
import com.sergio.taskmanager.tarefa.enums.StatusTarefa;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    @Transactional
    public String criar(TarefaRequestDTO dto) {
        if (dto.titulo() == null || dto.titulo().isBlank()) {
            throw new RuntimeException("Informe um título");
        }
        Tarefa tarefa = Tarefa.builder()
                .titulo(dto.titulo())
                .descricao(dto.descricao())
                .status(dto.status())
                .prioridade(dto.prioridade())
                .dataEntrega(dto.dataEntrega())
                .build();

        tarefaRepository.save(tarefa);
        return "Tarefa criada com sucesso!";
    }

    public TarefaResponseDTO ler(Long id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa nao encontrada"));
        return new TarefaResponseDTO(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getStatus(),
                tarefa.getPrioridade(),
                tarefa.getDataCriacao(),
                tarefa.getDataEntrega(),
                tarefa.getDataConclusao());

    }

    public List<TarefaResponseDTO> lerTodos() {
        List<Tarefa> tarefas = tarefaRepository.findAll();
        if (tarefas.isEmpty()) {
            throw new RuntimeException("Nao ha tarefas.");
        }
        return tarefas.stream()
                .map(tarefa -> new TarefaResponseDTO(
                        tarefa.getId(),
                        tarefa.getTitulo(),
                        tarefa.getDescricao(),
                        tarefa.getStatus(),
                        tarefa.getPrioridade(),
                        tarefa.getDataCriacao(),
                        tarefa.getDataEntrega(),
                        tarefa.getDataConclusao()
                ))
                .toList();
    }
    public List<Tarefa> filtrarPrioridade(PrioridadeTarefa prioridade){
        return tarefaRepository.findByPrioridade(prioridade);
    }
    public List<Tarefa> filtrarStatus(StatusTarefa status){
        return tarefaRepository.findByStatus(status);
    }
    @Transactional
    public void deletar(Long id) {
        tarefaRepository.deleteById(id);
    }
    @Transactional
    public TarefaResponseDTO atualizar(Long id, TarefaRequestDTO dto){
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Não existe uma tarefa com esse ID"));
        tarefa.setTitulo(dto.titulo());
        tarefa.setDescricao(dto.descricao());
        tarefa.setStatus(dto.status());
        tarefa.setPrioridade(dto.prioridade());
        tarefa.setDataEntrega(dto.dataEntrega());
        tarefaRepository.save(tarefa);
        TarefaResponseDTO tarefaResponseDTO= new TarefaResponseDTO(tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getStatus(),
                tarefa.getPrioridade(),
                tarefa.getDataCriacao(),
                tarefa.getDataEntrega(),
                tarefa.getDataConclusao()
                );

        return tarefaResponseDTO;
    }

}
