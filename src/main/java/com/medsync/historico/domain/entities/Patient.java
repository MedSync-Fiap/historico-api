package com.medsync.historico.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    private String id;
    private String nome;
    private String email;
    private String dataNascimento;
    private String cpf;
    private String observacoes;
    private Boolean ativo;
    private String criadoEm;
    private String atualizadoEm;

}
