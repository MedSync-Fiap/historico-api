package com.medsync.historico.domain.entities;

import com.medsync.historico.application.dto.AppointmentEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {

    private Long id;
    private String name;
    private String specialty;

    public Doctor(AppointmentEvent appointmentEvent) {
        this.id = appointmentEvent.medicoId();
        this.name = appointmentEvent.medicoNome();
        this.specialty = appointmentEvent.medicoEspecialidade();
    }

}
