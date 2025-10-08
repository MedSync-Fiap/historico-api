package com.medsync.historico.presentation.dto;

import java.time.LocalDateTime;

public record PatientResponse(
    String id,
    String nome,
    String cpf,
    String email,
    String dataNascimento,
    String observacoes,
    Boolean ativo,
    String criadoEm,
    String atualizadoEm
) {}
