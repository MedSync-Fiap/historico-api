package com.medsync.historico.infrastructure.persistence.document.embedded;

public record CreateUserInfo(
        Long id,
        String name,
        String email,
        String role
) {}
