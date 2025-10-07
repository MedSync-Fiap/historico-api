package com.medsync.historico.presentation.dto;

public record PatientInput(
    String nome,
    String cpf,
    String email,
    String dataNascimento,
    String observacoes
) {}
