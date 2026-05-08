package com.sergio.taskmanager.tarefa;

import com.sergio.taskmanager.exception.TarefaNaoEncontradaException;
import com.sergio.taskmanager.tarefa.dto.TarefaRequestDTO;
import com.sergio.taskmanager.tarefa.dto.TarefaResponseDTO;
import com.sergio.taskmanager.tarefa.enums.PrioridadeTarefa;
import com.sergio.taskmanager.tarefa.enums.StatusTarefa;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        if(dto.status()==null){
            throw new RuntimeException("Informe um status");
        }
        if(dto.prioridade()==null){
            throw new RuntimeException("Informe uma prioridade");
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
                .orElseThrow(() -> new TarefaNaoEncontradaException("Tarefa nao encontrada"));
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
            throw new TarefaNaoEncontradaException("Nao ha tarefas");
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

    public List<TarefaResponseDTO> filtrar(PrioridadeTarefa prioridade,StatusTarefa status){
        List<Tarefa> tarefas = List.of();
        if(prioridade!=null && status!=null){
            tarefas= tarefaRepository.findByPrioridadeAndStatus(prioridade,status);
        }
        else if(prioridade==null && status !=null){
            tarefas= tarefaRepository.findByStatus(status);
        }
        else if (prioridade !=null && status == null){
            tarefas=tarefaRepository.findByPrioridade(prioridade);
        }
        if(tarefas.isEmpty()){
            throw new TarefaNaoEncontradaException("Nao foram encontradas tarefas com esse(s) filtros");
        }else {
            return tarefas.stream().map(tarefa -> new TarefaResponseDTO(
                    tarefa.getId(),
                    tarefa.getTitulo(),
                    tarefa.getDescricao(),
                    tarefa.getStatus(),
                    tarefa.getPrioridade(),
                    tarefa.getDataCriacao(),
                    tarefa.getDataEntrega(),
                    tarefa.getDataConclusao()
            )).toList();
        }
    }

    @Transactional
    public void deletar(Long id) {
        Tarefa tarefa=tarefaRepository.findById(id).orElseThrow(
                ()->new TarefaNaoEncontradaException("Nao e possivel deletar uma tarefa inexistente"));
        tarefaRepository.delete(tarefa);
    }

    @Transactional
    public TarefaResponseDTO atualizar(Long id, TarefaRequestDTO dto){
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(()->new TarefaNaoEncontradaException("Não existe uma tarefa com esse ID"));
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
    @Transactional
    public TarefaResponseDTO concluir(Long id){
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(()->new TarefaNaoEncontradaException("Tarefa nao encontrada"));
        if(tarefa.getDataConclusao()!=null||tarefa.getStatus()==StatusTarefa.CONCLUIDA){
            throw  new RuntimeException("A tarefa ja esta concluida");
        }
        tarefa.setDataConclusao(LocalDateTime.now());
        tarefa.setStatus(StatusTarefa.CONCLUIDA);
        tarefaRepository.save(tarefa);

        return new TarefaResponseDTO(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getStatus(),
                tarefa.getPrioridade(),
                tarefa.getDataCriacao(),
                tarefa.getDataEntrega(),
                tarefa.getDataConclusao()
                );
    }

}
