package com.projetopoo.controller;

import com.projetopoo.model.Ferramenta;
import com.projetopoo.service.FerramentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ferramentas")
@RequiredArgsConstructor
public class FerramentaController {

    private final FerramentaService ferramentaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Ferramenta create(@Valid @RequestBody Ferramenta f) {
        return ferramentaService.salvar(f);
    }

    @GetMapping
    public List<Ferramenta> list() {
        return ferramentaService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ferramenta> get(@PathVariable Long id) {
        return ferramentaService.buscarPorId(id)
                .map(ResponseEntity::ok) // 200 OK
                .orElse(ResponseEntity.notFound().build()); // 404 Not Found
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ferramenta> update(@PathVariable Long id, @Valid @RequestBody Ferramenta f) {
        if (!ferramentaService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        f.setId(id);
        Ferramenta ferramentaAtualizada = ferramentaService.salvar(f);
        return ResponseEntity.ok(ferramentaAtualizada);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        ferramentaService.deletar(id);
    }

    @PostMapping("/{id}/emprestar")
    public ResponseEntity<Ferramenta> emprestar(@PathVariable Long id) {
        Ferramenta f = ferramentaService.emprestarFerramenta(id);
        if (f != null) {
            return ResponseEntity.ok(f);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/devolver")
    public ResponseEntity<Ferramenta> devolver(@PathVariable Long id) {
        Ferramenta f = ferramentaService.devolverFerramenta(id);
        if (f != null) {
            return ResponseEntity.ok(f);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public List<Ferramenta> search(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Long proprietarioId) {

        if (proprietarioId != null) {
            return ferramentaService.buscarPorProprietario(proprietarioId);
        }

        if (status != null && !status.isEmpty()) {
            return ferramentaService.buscarPorStatus(status);
        }

        if (nome != null && !nome.isEmpty()) {
            return ferramentaService.buscarPorNome(nome);
        }

        return ferramentaService.listarTodos();
    }
}