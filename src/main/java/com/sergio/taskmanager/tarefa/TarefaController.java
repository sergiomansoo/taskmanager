package com.sergio.taskmanager.tarefa;

import com.sergio.taskmanager.tarefa.dto.TarefaRequestDTO;
import com.sergio.taskmanager.tarefa.dto.TarefaResponseDTO;
import com.sergio.taskmanager.tarefa.enums.PrioridadeTarefa;
import com.sergio.taskmanager.tarefa.enums.StatusTarefa;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        tarefaService.deletar(id);
        return ResponseEntity.ok("Tarefa deletada com sucesso");
    }
    @PutMapping("/{id}")
    public ResponseEntity<TarefaResponseDTO> atualizar(@PathVariable Long id,
                                                       @RequestBody TarefaRequestDTO dto) {
        TarefaResponseDTO tarefaResponseDTO=tarefaService.atualizar(id,dto);
        return ResponseEntity.ok(tarefaResponseDTO);
    }
    @GetMapping("/filtrar")
    public List<Tarefa> filtrarPrioridade(@RequestParam PrioridadeTarefa prioridade){
        return tarefaService.filtrarPrioridade(prioridade);
    }
    @GetMapping("/filtrar")
    public List<Tarefa> filtrarStatus(@RequestParam StatusTarefa status){
        return tarefaService.filtrarStatus(status);
    }

}
