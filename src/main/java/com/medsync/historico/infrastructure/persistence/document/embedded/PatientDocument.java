package com.medsync.historico.infrastructure.persistence.document.embedded;

import java.time.LocalDate;

public record PatientDocument
(   Long id,
    String name,
    String cpf,
    String email,
    LocalDate dateOfBirth
) {}
