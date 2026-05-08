package com.sergio.taskmanager.usuario;

import com.sergio.taskmanager.exception.NotFoundException;
import com.sergio.taskmanager.tarefa.TarefaRepository;
import com.sergio.taskmanager.usuario.dto.UsuarioRequest;

import com.sergio.taskmanager.usuario.dto.UsuarioResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final TarefaRepository tarefaRepository;

    @Transactional
    public void criar(UsuarioRequest request){
        if(request.nome()==null|| request.nome().isBlank()){
            throw new RuntimeException("Informe um Nome");
        }
        if(request.email()==null||request.email().isBlank()){
            throw new RuntimeException("Informe um Email");
        }
        if(usuarioRepository.existsByEmail(request.email())){
            throw new RuntimeException("Email ja cadastrado");
        }
        if(request.senha()==null||request.senha().isBlank()||request.senha().length()<7){
            throw new RuntimeException("Senha deve possuir mais de 6 caracteres");
        }
        Usuario usuario= Usuario.builder().nome(request.nome())
                .email(request.email())
                .senha(request.senha())
                .build();
        usuarioRepository.save(usuario);
    }
    public UsuarioResponse ler(Long id){
        Usuario usuario=usuarioRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Usuario nao existe."));
        UsuarioResponse response= new UsuarioResponse(usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getData_criacao());
        return response;
    }
    public List<UsuarioResponse> lerTodos(){
        List<Usuario> usuarios=usuarioRepository.findAll();
        if(usuarios.isEmpty()){
            throw new NotFoundException("Usuario nao existe");
        }
        return usuarios.stream()
                .map(usuario->new UsuarioResponse(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getData_criacao()
                        )).toList();
    }
    @Transactional
    public void deletar(Long id){
        Usuario deletar=usuarioRepository.findById(id).
                orElseThrow(()->new RuntimeException("Nao e possivel deletar usuario inexistente"));
        if(tarefaRepository.existsById(id)){
            throw new RuntimeException("Nao e possivel deletar usuario vinculado a tarefas");
        }
        usuarioRepository.delete(deletar);
    }
    @Transactional
    public void atualizar(Long id, String nome, String email){
        Usuario usuario=usuarioRepository.findById(id).orElseThrow(
                ()->new RuntimeException("Usuario nao existe"));
        usuario.setNome(nome);
        if(usuarioRepository.existsByEmail(email)){
            throw new RuntimeException("Email ja cadastrado");
        }
        usuario.setEmail(email);
    }

}
