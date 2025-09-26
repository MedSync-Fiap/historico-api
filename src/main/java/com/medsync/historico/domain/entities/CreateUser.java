package com.medsync.historico.domain.entities;

import com.medsync.historico.application.dto.AppointmentEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUser {

    private Long id;
    private String name;
    private String email;
    private String role;

    public CreateUser(AppointmentEvent appointmentEvent) {
        this.id = appointmentEvent.usuarioId();
        this.name = appointmentEvent.usuarioNome();
        this.email = appointmentEvent.usuarioEmail();
        this.role = appointmentEvent.usuarioRole();
    }

}
