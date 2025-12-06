package com.projetopoo.repository;

import com.projetopoo.model.Proprietario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProprietarioRepository extends JpaRepository<Proprietario, Long> {

    List<Proprietario> findByNomeContaining(String nome);
}