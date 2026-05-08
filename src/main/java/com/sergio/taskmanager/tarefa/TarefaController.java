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
    public ResponseEntity<List<TarefaResponseDTO>> lerTodos() {
        return ResponseEntity.ok(tarefaService.lerTodos());
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<TarefaResponseDTO>> filtrarPrioridade(@RequestParam(required = false) PrioridadeTarefa prioridade,
                                                     @RequestParam(required = false) StatusTarefa status){
        return ResponseEntity.ok(tarefaService.filtrar(prioridade,status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaResponseDTO> ler(@PathVariable Long id) {
        TarefaResponseDTO tarefa = tarefaService.ler(id);
        return ResponseEntity.ok(tarefa);
    }
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<TarefaResponseDTO>> lerTarefasUsuario(@PathVariable Long usuarioId,
                                                                     @RequestParam(required = false) StatusTarefa status,
                                                                     @RequestParam(required = false) PrioridadeTarefa prioridade){
        return ResponseEntity.ok(tarefaService.lerTarefasUsuario(usuarioId,status,prioridade));
    }


    @PostMapping
    public ResponseEntity<String> criar(@RequestBody TarefaRequestDTO dto) {
        tarefaService.criar(dto);
        return ResponseEntity.ok("Tarefa criada com sucesso!");
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
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<TarefaResponseDTO> concluir(@PathVariable Long id){
        return ResponseEntity.ok(tarefaService.concluir(id));
    }



}
