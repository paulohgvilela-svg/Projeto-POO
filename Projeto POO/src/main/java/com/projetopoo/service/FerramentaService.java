package com.projetopoo.service;

import com.projetopoo.model.Ferramenta;
import com.projetopoo.repository.FerramentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FerramentaService {

    @Autowired
    private FerramentaRepository ferramentaRepository;

    public Ferramenta salvar(Ferramenta ferramenta) {
        return ferramentaRepository.save(ferramenta);
    }

    public List<Ferramenta> listarTodos() {
        return ferramentaRepository.findAll();
    }

    public Optional<Ferramenta> buscarPorId(Long id) {
        return ferramentaRepository.findById(id);
    }

    public void deletar(Long id) {
        ferramentaRepository.deleteById(id);
    }

    public List<Ferramenta> buscarPorStatus(String status) {
        return ferramentaRepository.findByStatus(status);
    }

    public List<Ferramenta> buscarPorNome(String nome) {
        return ferramentaRepository.findByNomeContaining(nome);
    }

    public List<Ferramenta> buscarPorProprietario(Long proprietarioId) {
        return ferramentaRepository.findByProprietarioId(proprietarioId);
    }

    /**
     * Método auxiliar privado que centraliza a lógica de busca e atualização de status,
     * reduzindo a repetição de código (DRY).
     */
    private Ferramenta atualizarStatus(Long id, String novoStatus) {
        return ferramentaRepository.findById(id)
                .map(f -> {

                    f.setStatus(novoStatus);

                    return ferramentaRepository.save(f);
                })
                .orElse(null);
    }

    public Ferramenta emprestarFerramenta(Long id) {
        return atualizarStatus(id, "emprestado");
    }

    public Ferramenta devolverFerramenta(Long id) {
        return atualizarStatus(id, "disponivel");
    }
}