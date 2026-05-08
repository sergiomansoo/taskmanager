package com.sergio.taskmanager.tarefa;

import com.sergio.taskmanager.exception.NotFoundException;
import com.sergio.taskmanager.exception.NotFoundException;
import com.sergio.taskmanager.tarefa.dto.TarefaRequestDTO;
import com.sergio.taskmanager.tarefa.dto.TarefaResponseDTO;
import com.sergio.taskmanager.tarefa.enums.PrioridadeTarefa;
import com.sergio.taskmanager.tarefa.enums.StatusTarefa;
import com.sergio.taskmanager.usuario.UsuarioRepository;
import jakarta.transaction.Status;
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
    private final UsuarioRepository usuarioRepository;
    @Transactional
    public void criar(TarefaRequestDTO dto) {
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
                .usuario(usuarioRepository.findById(dto.usuarioId()).orElseThrow(
                        ()->new RuntimeException("Nao e possivel criar tarefa para um usuario inexistente")
                ))
                .build();

        tarefaRepository.save(tarefa);
    }

    public TarefaResponseDTO ler(Long id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tarefa nao encontrada"));
        return new TarefaResponseDTO(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getStatus(),
                tarefa.getPrioridade(),
                tarefa.getDataCriacao(),
                tarefa.getDataEntrega(),
                tarefa.getDataConclusao(),
                tarefa.getUsuario().getId()
        );

    }

    public List<TarefaResponseDTO> lerTodos() {
        List<Tarefa> tarefas = tarefaRepository.findAll();
        if (tarefas.isEmpty()) {
            throw new NotFoundException("Nao ha tarefas");
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
                        tarefa.getDataConclusao(),
                        tarefa.getUsuario().getId()
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
            throw new NotFoundException("Nao foram encontradas tarefas com esse(s) filtros");
        }else {
            return tarefas.stream().map(tarefa -> new TarefaResponseDTO(
                    tarefa.getId(),
                    tarefa.getTitulo(),
                    tarefa.getDescricao(),
                    tarefa.getStatus(),
                    tarefa.getPrioridade(),
                    tarefa.getDataCriacao(),
                    tarefa.getDataEntrega(),
                    tarefa.getDataConclusao(),
                    tarefa.getUsuario().getId()
            )).toList();
        }
    }

    @Transactional
    public void deletar(Long id) {
        Tarefa tarefa=tarefaRepository.findById(id).orElseThrow(
                ()->new NotFoundException("Nao e possivel deletar uma tarefa inexistente"));
        tarefaRepository.delete(tarefa);
    }

    @Transactional
    public TarefaResponseDTO atualizar(Long id, TarefaRequestDTO dto){
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Não existe uma tarefa com esse ID"));
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
                tarefa.getDataConclusao(),
                tarefa.getUsuario().getId()
                );
        return tarefaResponseDTO;
    }
    @Transactional
    public TarefaResponseDTO concluir(Long id){
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Tarefa nao encontrada"));
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
                tarefa.getDataConclusao(),
                tarefa.getUsuario().getId()
                );
    }
    public List<TarefaResponseDTO> lerTarefasUsuario(Long id, StatusTarefa status,
                                                     PrioridadeTarefa prioridadeTarefa){
        if(!tarefaRepository.existsById(id)){
            throw new NotFoundException("Nao existe esse usuario");
        }
        List<Tarefa> tarefas=tarefaRepository.findByUsuarioId(id);
        if(tarefas.isEmpty()){
            throw new NotFoundException("Não ha tarefas para esse usuario");
        }
        if(status==null&&prioridadeTarefa!=null){
            tarefas=tarefaRepository.findByUsuarioIdAndPrioridade(id,prioridadeTarefa);
        }
        else if(status!=null&&prioridadeTarefa==null){
            tarefas=tarefaRepository.findByUsuarioIdAndStatus(id,status);
        }
        else if(status!=null&&prioridadeTarefa!=null){
            tarefas=tarefaRepository.findByUsuarioIdAndPrioridadeAndStatus(id,prioridadeTarefa,status);
        }

        return tarefas.stream().map(tarefa -> new TarefaResponseDTO(tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getStatus(),
                tarefa.getPrioridade(),
                tarefa.getDataCriacao(),
                tarefa.getDataEntrega(),
                tarefa.getDataConclusao(),
                tarefa.getUsuario().getId())).toList();
    }


}
