package com.medsync.historico.infrastructure.persistence.document.embedded;

import com.medsync.historico.domain.enums.ActionType;

import java.time.LocalDateTime;

public record ActionLogDocument(
        ActionType actionType,
        CreateUserDocument user,
        LocalDateTime timestamp
) {
}
