package com.medsync.historico.infrastructure.persistence.document.embedded;

public record DoctorInfo(
        String id,
        String name,
        String specialty
) {
}
