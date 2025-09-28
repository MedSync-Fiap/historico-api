package com.medsync.historico.infrastructure.persistence.document.embedded;

public record CreateUserDocument(
        Long id,
        String name,
        String email,
        String role
) {}
