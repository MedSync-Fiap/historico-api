package com.medsync.historico.infrastructure.persistence.document.embedded;

public record CreateUserDocument(
        String id,
        String name,
        String email,
        String role
) {}
