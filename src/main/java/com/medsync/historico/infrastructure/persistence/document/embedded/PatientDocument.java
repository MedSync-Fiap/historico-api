package com.medsync.historico.infrastructure.persistence.document.embedded;

import java.time.LocalDate;

public record PatientDocument
(   String id,
    String name,
    String cpf,
    String email,
    LocalDate dateOfBirth,
    String observacoes,
    Boolean ativo,
    String criadoEm,
    String atualizadoEm
) {}
