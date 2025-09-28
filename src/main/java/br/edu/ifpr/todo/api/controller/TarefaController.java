package br.edu.ifpr.todo.api.controller;

import br.edu.ifpr.todo.api.dto.TarefaRequest;
import br.edu.ifpr.todo.api.dto.TarefaResponse;
import br.edu.ifpr.todo.domain.model.Tarefa;
import br.edu.ifpr.todo.domain.model.TodoStatus;
import br.edu.ifpr.todo.domain.service.TarefaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {
    private final TarefaService service;

    public TarefaController(TarefaService service) {
        this.service = service;
    }

    /**
     * Criar nova tarefa
     * POST /api/tarefas
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TarefaResponse criar(@Valid @RequestBody TarefaRequest dto) {
        Tarefa tarefa = service.criar(dto);
        return toResponse(tarefa);
    }

    /**
     * Listar todas as tarefas
     * GET /api/tarefas
     */
    @GetMapping
    public List<TarefaResponse> listar() {
        List<Tarefa> tarefas = service.listar(null, null, null, null);
        return tarefas.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Filtrar tarefas por status
     * GET /api/tarefas?status=FAZENDO
     */
    @GetMapping(params = "status")
    public List<TarefaResponse> filtrarPorStatus(@RequestParam TodoStatus status) {
        List<Tarefa> tarefas = service.listar(null, status, null, null);
        return tarefas.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Filtrar tarefas importantes
     * GET /api/tarefas?importante=true
     */
    @GetMapping(params = "importante")
    public List<TarefaResponse> filtrarPorImportancia(@RequestParam Boolean importante) {
        List<Tarefa> tarefas = service.listar(null, null, importante, null);
        return tarefas.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Buscar tarefa por ID
     * GET /api/tarefas/{id}
     */
    @GetMapping("/{id}")
    public TarefaResponse buscarPorId(@PathVariable Long id) {
        Tarefa tarefa = service.buscarPorId(id);
        return toResponse(tarefa);
    }

    /**
     * Atualização parcial de tarefa
     * PATCH /api/tarefas/{id}
     */
    @PatchMapping("/{id}")
    public TarefaResponse atualizarParcial(@PathVariable Long id, @RequestBody TarefaRequest dto) {
        // Para PATCH, precisamos de um método especial que só atualiza campos não nulos
        Tarefa tarefa = service.atualizarParcial(id, dto);
        return toResponse(tarefa);
    }

    /**
     * Excluir tarefa
     * DELETE /api/tarefas/{id}
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.remover(id);
    }

    /**
     * Método auxiliar para converter Tarefa em TarefaResponse
     */
    private TarefaResponse toResponse(Tarefa tarefa) {
        return new TarefaResponse(
            tarefa.getId(),
            tarefa.getNome(),
            tarefa.getDescricao(),
            tarefa.getStatus(),
            tarefa.getDataCriacao(),
            tarefa.getDataEntrega(),
            tarefa.getImportante()
        );
    }

    // Mantendo o endpoint GET para compatibilidade (se necessário)
    @GetMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public TarefaResponse criarViaGet(
            @RequestParam String nome,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false, defaultValue = "A_FAZER") TodoStatus status,
            @RequestParam(required = false) Boolean importante,
            @RequestParam(required = false) String dataEntrega) {
        var dto = new TarefaRequest();
        dto.setNome(nome);
        dto.setDescricao(descricao);
        dto.setStatus(status);
        dto.setImportante(importante);
        if (dataEntrega != null && !dataEntrega.isBlank()) {
            dto.setDataEntrega(LocalDate.parse(dataEntrega)); // yyyy-MM-dd
        }
        Tarefa salvo = service.criar(dto);
        return toResponse(salvo);
    }
}