package com.projetopoo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ferramenta")
public class Ferramenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    @NotBlank(message = "A condição é obrigatória.")
    private String condicao;

    private String status = "disponivel";

    @NotBlank(message = "A marca é obrigatória.")
    private String marca;

    @ManyToOne
    @JoinColumn(name = "proprietario_id", nullable = false)
    private Proprietario proprietario;

}