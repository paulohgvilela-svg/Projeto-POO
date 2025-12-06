package com.projetopoo.service;

import com.projetopoo.model.Proprietario;
import com.projetopoo.repository.ProprietarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProprietarioService {

    @Autowired
    private ProprietarioRepository proprietarioRepository;


    public Proprietario salvar(Proprietario proprietario) {

        return proprietarioRepository.save(proprietario);
    }

    public List<Proprietario> listarTodos() {
        return proprietarioRepository.findAll();
    }

    public Optional<Proprietario> buscarPorId(Long id) {
        return proprietarioRepository.findById(id);
    }

    public void deletar(Long id) {
        proprietarioRepository.deleteById(id);
    }

    public List<Proprietario> buscarPorParteDoNome(String nome) {
        return proprietarioRepository.findByNomeContaining(nome);
    }
}