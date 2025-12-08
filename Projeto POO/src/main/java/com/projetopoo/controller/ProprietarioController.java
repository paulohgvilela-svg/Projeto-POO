package com.projetopoo.controller;

import com.projetopoo.model.Proprietario;
import com.projetopoo.service.ProprietarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proprietarios")
@RequiredArgsConstructor
public class ProprietarioController {

    private final ProprietarioService proprietarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Proprietario create(@Valid @RequestBody Proprietario p) {
        return proprietarioService.salvar(p);
    }

    @GetMapping
    public List<Proprietario> list() {
        return proprietarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proprietario> get(@PathVariable Long id) {
        return proprietarioService.buscarPorId(id)
                .map(ResponseEntity::ok) // 200 OK
                .orElse(ResponseEntity.notFound().build()); // 404 Not Found
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proprietario> update(@PathVariable Long id, @Valid @RequestBody Proprietario p) {
        if (!proprietarioService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        p.setId(id);
        Proprietario proprietarioAtualizado = proprietarioService.salvar(p);
        return ResponseEntity.ok(proprietarioAtualizado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Código 204
    public void delete(@PathVariable Long id) {
        proprietarioService.deletar(id);
    }

    @GetMapping("/search")
    public List<Proprietario> search(
            @RequestParam(required = false) String nome) {

        if (nome != null && !nome.isEmpty()) {
            return proprietarioService.buscarPorParteDoNome(nome);
        }

        return proprietarioService.listarTodos();
    }
}