package com.projetopoo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "proprietario")
public class Proprietario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do proprietário é obrigatório.")
    @Size(min = 3, max = 150, message = "O nome deve ter entre 3 e 150 caracteres.")
    private String nome;

    @NotBlank(message = "O contato é obrigatório.")
    private String contato;

    @NotBlank(message = "O endereço é obrigatório.")
    private String endereco;

    @NotBlank(message = "O CPF é obrigatório.")
    @Size(min = 11, max = 14, message = "O CPF deve ter entre 11 e 14 caracteres.")
    private String cpf;

}