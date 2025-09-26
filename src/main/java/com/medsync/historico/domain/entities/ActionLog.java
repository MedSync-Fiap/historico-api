package com.medsync.historico.domain.entities;

import com.medsync.historico.application.dto.AppointmentEvent;
import com.medsync.historico.domain.enums.ActionType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ActionLog {

    private ActionType type;
    private CreateUser user;
    private LocalDateTime timestamp;

    public ActionLog(AppointmentEvent event, ActionType type) {
        this.type = type;
        this.user = new CreateUser(event);
        this.timestamp = event.timestamp();
    }
}
