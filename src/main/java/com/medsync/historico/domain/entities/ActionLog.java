package com.medsync.historico.domain.entities;

import com.medsync.historico.domain.enums.ActionType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ActionLog {

    private ActionType type;
    private CreateUser user;
    private Map<String, Object> data;
    private LocalDateTime createdAt;

}
