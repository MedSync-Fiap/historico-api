package com.medsync.historico.infrastructure.persistence.document.embedded;

import com.medsync.historico.domain.enums.ActionType;

import java.time.LocalDateTime;
import java.util.Map;

public record ActionLog(
        ActionType type,
        CreateUserInfo user,
        Map<String, Object> data,
        LocalDateTime timestamp
) {
}
