package com.medsync.historico.domain.entities;

import com.medsync.historico.application.dto.AppointmentInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    private String id;
    private String name;
    private String email;
    private LocalDate dateOfBirth;
    private String cpf;

    public Patient(AppointmentInput input) {
        this.id = input.pacienteId();
        this.name = input.pacienteNome();
        this.email = input.pacienteEmail();
        this.dateOfBirth = input.pacienteDataNascimento();
        this.cpf = input.pacienteCpf();
    }

}
