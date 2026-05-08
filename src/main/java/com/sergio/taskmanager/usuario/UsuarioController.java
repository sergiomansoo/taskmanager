package com.sergio.taskmanager.usuario;


import com.sergio.taskmanager.usuario.dto.UsuarioRequest;
import com.sergio.taskmanager.usuario.dto.UsuarioResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;
    UsuarioController(UsuarioService usuarioService){
        this.usuarioService=usuarioService;
    }
    @PostMapping
    public ResponseEntity<String> criar(@RequestBody UsuarioRequest usuarioRequest){
        usuarioService.criar(usuarioRequest);
        return ResponseEntity.ok("Usuario criado com sucesso!");
    }
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> ler(@PathVariable Long id){
        return ResponseEntity.ok(usuarioService.ler(id));
    }
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> lerTodos(){
        List<UsuarioResponse> usuarios=usuarioService.lerTodos();
        return ResponseEntity.ok(usuarios);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id){
        usuarioService.deletar(id);
        return ResponseEntity.ok("Usuario deletado com sucesso!");
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> atualizar(@PathVariable Long id,
                                            @RequestBody String nome, @RequestBody String email){
        usuarioService.atualizar(id,nome,email);
        return ResponseEntity.ok("Usuario atualizado com sucesso!");
    }

}
