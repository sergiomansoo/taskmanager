package com.sergio.taskmanager.usuario;

import com.sergio.taskmanager.exception.ForbiddenException;
import com.sergio.taskmanager.exception.NotFoundException;
import com.sergio.taskmanager.tarefa.TarefaRepository;
import com.sergio.taskmanager.usuario.dto.UsuarioRequest;

import com.sergio.taskmanager.usuario.dto.UsuarioResponse;
import com.sergio.taskmanager.usuario.dto.UsuarioUpdateRequest;
import com.sergio.taskmanager.usuario.enums.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final TarefaRepository tarefaRepository;
    private final PasswordEncoder passwordEncoder;


    private boolean isAdmin(Usuario usuario) {
        return usuario.getRole() == Role.ADMIN;
    }
    private Usuario getUsuarioLogado() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return usuarioRepository.findByEmail(email);
    }


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
                .senha(passwordEncoder.encode(request.senha()))
                .role(request.role() == null ? Role.USER : request.role())
                .build();
        usuarioRepository.save(usuario);
    }
    public UsuarioResponse ler(Long id){
        Usuario logado=getUsuarioLogado();
        if(!isAdmin(logado)&&!logado.getId().equals(id)){
            throw new RuntimeException("Nao e possivel alterar id que nao e seu");
        }
        Usuario usuario=usuarioRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Usuario nao existe."));
        UsuarioResponse response= new UsuarioResponse(usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getData_criacao(),
                usuario.getRole());
        return response;
    }
    public UsuarioResponse me() {
        Usuario usuario = getUsuarioLogado();

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getData_criacao(),
                usuario.getRole()
        );
    }

    public List<UsuarioResponse> lerTodos(){
        List<Usuario> usuarios=usuarioRepository.findAll();
        if(!isAdmin(getUsuarioLogado())){
            throw new ForbiddenException("Voce nao possui permissao para isso");
        }
        if(usuarios.isEmpty()){
            throw new NotFoundException("Usuario nao existe");
        }
        return usuarios.stream()
                .map(usuario->new UsuarioResponse(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getData_criacao(),
                        usuario.getRole()
                        )).toList();
    }
    @Transactional
    public void deletar(Long id){
        if (!isAdmin(getUsuarioLogado())){
            throw new ForbiddenException("Voce não possui permissão para deletar um usuario");
        }
        Usuario deletar=usuarioRepository.findById(id).
                orElseThrow(()->new RuntimeException("Nao é possivel deletar usuario inexistente"));
        if(tarefaRepository.existsByUsuarioId(id)){
            throw new RuntimeException("Nao é possivel deletar usuario vinculado a tarefas");
        }
        usuarioRepository.delete(deletar);
    }
    @Transactional
    public void atualizar(Long id, UsuarioUpdateRequest request){
        Usuario usuario=usuarioRepository.findById(id).orElseThrow(
                ()->new RuntimeException("Usuario nao existe"));
        if(!isAdmin(getUsuarioLogado())&& !id.equals(getUsuarioLogado().getId())){
            throw new ForbiddenException("Nao é possivel atualizar outro usuario");
        }
        atualizarDadosUsuario(usuario, request);
    }
    @Transactional
    public void atualizarMe(UsuarioUpdateRequest request){
        Usuario logado= getUsuarioLogado();
        atualizarDadosUsuario(logado, request);
    }

    private void atualizarDadosUsuario(Usuario usuario, UsuarioUpdateRequest request) {
        if (request.nome() != null && !request.nome().isBlank()) {
            usuario.setNome(request.nome());
        }
        if (request.email() == null || request.email().isBlank() || request.email().equals(usuario.getEmail())) {
            return;
        }
        if(usuarioRepository.existsByEmail(request.email())){
            throw new RuntimeException("Email ja cadastrado");
        }
        usuario.setEmail(request.email());
    }


}
