package com.medsync.historico.domain.entities;

import com.medsync.historico.application.dto.AppointmentEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    private Long id;
    private String name;
    private String email;
    private LocalDate dateOfBirth;
    private String cpf;

    public Patient(AppointmentEvent event) {
        this.id = event.pacienteId();
        this.name = event.pacienteNome();
        this.email = event.pacienteEmail();
        this.dateOfBirth = event.pacienteDataNascimento();
        this.cpf = event.pacienteCpf();
    }

}
