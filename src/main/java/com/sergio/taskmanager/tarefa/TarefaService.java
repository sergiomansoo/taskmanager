package com.sergio.taskmanager.tarefa;

import com.sergio.taskmanager.exception.ForbiddenException;
import com.sergio.taskmanager.exception.NotFoundException;
import com.sergio.taskmanager.exception.NotFoundException;
import com.sergio.taskmanager.tarefa.dto.TarefaRequestDTO;
import com.sergio.taskmanager.tarefa.dto.TarefaResponseDTO;
import com.sergio.taskmanager.tarefa.enums.PrioridadeTarefa;
import com.sergio.taskmanager.tarefa.enums.StatusTarefa;
import com.sergio.taskmanager.usuario.Usuario;
import com.sergio.taskmanager.usuario.UsuarioRepository;
import com.sergio.taskmanager.usuario.enums.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final UsuarioRepository usuarioRepository;
    private Usuario getUsuarioLogado(){
        String email= SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return usuarioRepository.findByEmail(email);
    }
    private boolean isAdmin(Usuario usuario){
        return usuario.getRole()==Role.ADMIN;
    }
    @Transactional
    public void criar(TarefaRequestDTO dto) {
        Usuario logado=getUsuarioLogado();
        Usuario donoDaTarefa = logado;
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
                .usuario(donoDaTarefa)
                .build();

        tarefaRepository.save(tarefa);
    }
    public void criarId(Long id, TarefaRequestDTO dto){
        Usuario logado=getUsuarioLogado();
        Usuario donoDaTarefa = logado;
        if(!id.equals(logado.getId())&&!isAdmin(logado)){
            throw new ForbiddenException("Voce nao possui permissao para criar uma tarefa para outro usuario");
        }
        donoDaTarefa = usuarioRepository.findById(id).orElseThrow(
                    ()->new RuntimeException("Nao e possivel criar tarefa para um usuario inexistente")
            );
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
                .usuario(donoDaTarefa)
                .build();

    }

    public TarefaResponseDTO ler(Long id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tarefa nao encontrada"));
        Usuario usuarioLogado=getUsuarioLogado();
        if(usuarioLogado.getRole()!=Role.ADMIN&&!tarefa.getUsuario().getId().equals(usuarioLogado.getId())){
            throw new ForbiddenException("Nao e possivel acessar essa tarefa");
        }
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
        Usuario usuarioLogado=getUsuarioLogado();
        List<Tarefa> tarefas=List.of();
        if(isAdmin(usuarioLogado)){
            tarefas=tarefaRepository.findAll();
        }
        else{
            tarefas=tarefaRepository.findByUsuarioId(usuarioLogado.getId());
        }
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
        if(isAdmin(getUsuarioLogado())) {
            if (prioridade != null && status != null) {
                tarefas = tarefaRepository.findByPrioridadeAndStatus(prioridade, status);
            } else if (prioridade == null && status != null) {
                tarefas = tarefaRepository.findByStatus(status);
            } else if (prioridade != null && status == null) {
                tarefas = tarefaRepository.findByPrioridade(prioridade);
            } else {
                tarefas = tarefaRepository.findAll();
            }
        }
        else {
            Long usuarioId = getUsuarioLogado().getId();

            if (prioridade != null && status != null) {
                tarefas = tarefaRepository.findByUsuarioIdAndPrioridadeAndStatus(usuarioId, prioridade, status);
            } else if (prioridade == null && status != null) {
                tarefas = tarefaRepository.findByUsuarioIdAndStatus(usuarioId, status);
            } else if (prioridade != null) {
                tarefas = tarefaRepository.findByUsuarioIdAndPrioridade(usuarioId, prioridade);
            } else {
                tarefas = tarefaRepository.findByUsuarioId(usuarioId);
            }
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
        Usuario logado=getUsuarioLogado();
        Tarefa tarefa=tarefaRepository.findById(id).orElseThrow(
                ()->new NotFoundException("Nao e possivel deletar uma tarefa inexistente"));
        if(!isAdmin(logado) && !tarefa.getUsuario().getId().equals(logado.getId())){
            throw new ForbiddenException("Nao e possivel deletar tarefa que nao é sua");
        }
        tarefaRepository.delete(tarefa);
    }

    @Transactional
    public TarefaResponseDTO atualizar(Long id, TarefaRequestDTO dto){
        Usuario logado= getUsuarioLogado();

        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Não existe uma tarefa com esse ID"));
        if(!isAdmin(logado) && !tarefa.getUsuario().getId().equals(logado.getId())){
            throw new ForbiddenException("Nao e possivel atualizar tarefa que nao é sua");
        }
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
        Usuario logado=getUsuarioLogado();
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Tarefa nao encontrada"));
        if(!isAdmin(logado) && !tarefa.getUsuario().getId().equals(logado.getId())){
            throw new ForbiddenException("Nao e possivel concluir tarefa que nao é sua");
        }
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
        Usuario logado=getUsuarioLogado();
        if(!isAdmin(logado)&&!id.equals(logado.getId())){
            throw new ForbiddenException("Nao é possivel acessar");
        }
        if(!usuarioRepository.existsById(id)){
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
