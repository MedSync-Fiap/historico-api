package com.medsync.historico.domain.entities;

import com.medsync.historico.application.dto.AppointmentInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {

    private String id;
    private String name;
    private String specialty;

    public Doctor(AppointmentInput input) {
        this.id = input.medicoId();
        this.name = input.medicoNome();
        this.specialty = input.especialidadeNome();
    }

}
