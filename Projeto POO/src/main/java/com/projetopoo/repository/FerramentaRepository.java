package com.projetopoo.repository;

import com.projetopoo.model.Ferramenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FerramentaRepository extends JpaRepository<Ferramenta, Long> {

    List<Ferramenta> findByStatus(String status);

    List<Ferramenta> findByNomeContaining(String nome);

    List<Ferramenta> findByProprietarioId(Long proprietarioId);
}