package com.medsync.historico.infrastructure.persistence.document.embedded;

public record DoctorDocument(
        String id,
        String name,
        String specialty
) {
}
