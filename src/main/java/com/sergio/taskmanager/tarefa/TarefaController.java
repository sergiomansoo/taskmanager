package com.sergio.taskmanager.tarefa;

import com.sergio.taskmanager.tarefa.dto.TarefaRequestDTO;
import com.sergio.taskmanager.tarefa.dto.TarefaResponseDTO;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tarefa")
public class TarefaController {

    private final TarefaService tarefaService;

    TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @GetMapping
    public List<TarefaResponseDTO> lerTodos() {
        return tarefaService.lerTodos();
    }
    @Transactional
    @PostMapping
    public ResponseEntity<String> criar(@RequestBody TarefaRequestDTO dto) {
        tarefaService.criar(dto);
        return ResponseEntity.ok("Tarefa criada com sucesso!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaResponseDTO> ler(@PathVariable Long id) {
        TarefaResponseDTO tarefa = tarefaService.ler(id);
        return ResponseEntity.ok(tarefa);
    }
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        tarefaService.deletar(id);
        return ResponseEntity.ok().build();
    }
    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<TarefaResponseDTO> atualizar(@PathVariable Long id,
                                                                @RequestBody TarefaRequestDTO dto) {
        TarefaResponseDTO tarefaResponseDTO=tarefaService.atualizar(id,dto);
        return ResponseEntity.ok(tarefaResponseDTO);
    }

}
