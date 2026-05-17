package com.sergio.taskmanager.usuario;


import com.sergio.taskmanager.usuario.dto.UsuarioRequest;
import com.sergio.taskmanager.usuario.dto.UsuarioResponse;
import com.sergio.taskmanager.usuario.dto.UsuarioUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuários", description = "Consulta e administração de usuários")
@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;
    UsuarioController(UsuarioService usuarioService){
        this.usuarioService=usuarioService;
    }
    @Operation(summary = "Criar usuário", description = "ADMIN cria usuario atraves da passagem de parametros")
    @PostMapping
    public ResponseEntity<String> criar(@RequestBody UsuarioRequest usuarioRequest){
        usuarioService.criar(usuarioRequest);
        return ResponseEntity.ok("Usuario criado com sucesso!");
    }
    @Operation(summary = "Meu perfil", description = "Retorna os dados do usuário autenticado.")
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> me() {
        return ResponseEntity.ok(usuarioService.me());
    }
    @Operation(summary = "Buscar usuário", description = "Retorna os dados do usuário autenticado.")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> ler(@PathVariable Long id){
        return ResponseEntity.ok(usuarioService.ler(id));
    }
    @Operation(summary = "Buscar todos os usuários", description = "Retorna os dados de todos os usuarios autenticados caso o usuario logado tenha permissao.")
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> lerTodos() {
        List<UsuarioResponse> usuarios = usuarioService.lerTodos();
        return ResponseEntity.ok(usuarios);
    }
    @Operation(summary = "Deletar usuário", description = "Deleta usuario caso o logado tenha permissao")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id){
        usuarioService.deletar(id);
        return ResponseEntity.ok("Usuario deletado com sucesso!");
    }
    @Operation(summary = "Atualiza usuário por id", description = "Atualiza usuario passando seu id caso logado tenha permissao")
    @PutMapping("/{id}")
    public ResponseEntity<String> atualizar(@PathVariable Long id, @RequestBody UsuarioUpdateRequest request){
        usuarioService.atualizar(id, request);
        return ResponseEntity.ok("Usuario atualizado com sucesso!");
    }
    @Operation(summary = "Atualiza usuario logado", description = "Usuario logado no sistema atualiza seus atributos")
    @PutMapping
    public ResponseEntity<String> atualizarMe(@RequestBody UsuarioUpdateRequest request){
        usuarioService.atualizarMe(request);
        return ResponseEntity.ok("Usuario atualizado com sucesso!");
    }

}
