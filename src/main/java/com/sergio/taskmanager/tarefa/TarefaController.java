package com.sergio.taskmanager.tarefa;

import com.sergio.taskmanager.tarefa.dto.TarefaRequestDTO;
import com.sergio.taskmanager.tarefa.dto.TarefaResponseDTO;
import com.sergio.taskmanager.tarefa.enums.PrioridadeTarefa;
import com.sergio.taskmanager.tarefa.enums.StatusTarefa;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Tarefas", description = "Gerenciamento de tarefas do usuário autenticado")
@RestController
@RequestMapping("/tarefa")
public class TarefaController {

    private final TarefaService tarefaService;

    TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }
    @Operation(summary = "Usuario cria tarefa")
    @PostMapping
    public ResponseEntity<String> criar(@RequestBody TarefaRequestDTO dto) {
        tarefaService.criar(dto);
        return ResponseEntity.ok("Tarefa criada com sucesso!");
    }
    @PostMapping("/{id}")
    public ResponseEntity<String> criarId(@RequestParam Long id,@RequestBody TarefaRequestDTO dto) {
        tarefaService.criarId(id,dto);
        return ResponseEntity.ok("Tarefa criada com sucesso!");
    }
    @Operation(summary = "Listar tarefas", description = "USER visualiza apenas suas próprias tarefas. ADMIN visualiza todas.")
    @GetMapping
    public ResponseEntity<List<TarefaResponseDTO>> lerTodos() {
        return ResponseEntity.ok(tarefaService.lerTodos());
    }
    @Operation(summary = "Filtrar tarefas", description = "USER filtra apenas suas próprias tarefas. ADMIN filtra todas.")
    @GetMapping("/filtrar")
    public ResponseEntity<List<TarefaResponseDTO>> filtrarPrioridade(@RequestParam(required = false) PrioridadeTarefa prioridade,
                                                     @RequestParam(required = false) StatusTarefa status){
        return ResponseEntity.ok(tarefaService.filtrar(prioridade,status));
    }
    @Operation(summary = "Buscar tarefa por ID", description = "USER visualiza apenas se a tarefa é dele. ADMIN visualiza qualquer uma.")
    @GetMapping("/{id}")
    public ResponseEntity<TarefaResponseDTO> ler(@PathVariable Long id) {
        TarefaResponseDTO tarefa = tarefaService.ler(id);
        return ResponseEntity.ok(tarefa);
    }
    @Operation(summary = "Busca todas as tarefas de certo USER", description = "ADMIN visualiza tarefas de um certo USER.")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<TarefaResponseDTO>> lerTarefasUsuario(@PathVariable Long usuarioId,
                                                                     @RequestParam(required = false) StatusTarefa status,
                                                                     @RequestParam(required = false) PrioridadeTarefa prioridade){
        return ResponseEntity.ok(tarefaService.lerTarefasUsuario(usuarioId,status,prioridade));
    }


    @Operation(summary = "Deleta tarefa por ID", description = "USER deleta apenas se é dele. ADMIN deleta qualquer tarefa.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        tarefaService.deletar(id);
        return ResponseEntity.ok("Tarefa deletada com sucesso");
    }
    @Operation(summary = "Atualiza tarefa por ID", description = "USER atualiza apenas se é dele. ADMIN atualiza qualquer tarefa.")
    @PutMapping("/{id}")
    public ResponseEntity<TarefaResponseDTO> atualizar(@PathVariable Long id,
                                                       @RequestBody TarefaRequestDTO dto) {
        TarefaResponseDTO tarefaResponseDTO=tarefaService.atualizar(id,dto);
        return ResponseEntity.ok(tarefaResponseDTO);
    }
    @Operation(summary = "Conclui tarefa por ID", description = "USER conclui apenas se é dele. ADMIN conclui qualquer tarefa.")
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<TarefaResponseDTO> concluir(@PathVariable Long id){
        return ResponseEntity.ok(tarefaService.concluir(id));
    }



}
