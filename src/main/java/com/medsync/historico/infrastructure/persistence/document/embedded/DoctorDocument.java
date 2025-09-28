package com.medsync.historico.infrastructure.persistence.document.embedded;

public record DoctorDocument(
        Long id,
        String name,
        String specialty
) {
}
