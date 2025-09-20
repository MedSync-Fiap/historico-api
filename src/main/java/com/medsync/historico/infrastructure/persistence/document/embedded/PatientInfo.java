package com.medsync.historico.infrastructure.persistence.document.embedded;

import java.util.List;

public record PatientInfo
(   String id,
    String name,
    String cpf,
    String email,
    List<Phone> phones
) {}
