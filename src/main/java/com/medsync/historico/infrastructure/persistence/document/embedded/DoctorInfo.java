package com.medsync.historico.infrastructure.persistence.document.embedded;

public record DoctorInfo(
        Long id,
        String name,
        String specialty
) {
}
