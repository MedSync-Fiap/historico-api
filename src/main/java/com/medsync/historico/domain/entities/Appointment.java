package com.medsync.historico.domain.entities;

import com.medsync.historico.application.dto.AppointmentInput;
import com.medsync.historico.domain.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {

    private String id;
    private Doctor doctor;
    private CreateUser createUser;
    private LocalDateTime appointmentDateTime;
    private AppointmentStatus status;
    private String clinicalNotes;
    private List<ActionLog> actionLogs;

    public Appointment(AppointmentInput newAppointmentInput, List<ActionLog> actionLogs) {
        this.id = newAppointmentInput.consultaId();
        this.doctor = new Doctor(newAppointmentInput);
        this.createUser = new CreateUser(newAppointmentInput);
        this.appointmentDateTime = newAppointmentInput.dataHora();
        this.status = AppointmentStatus.AGENDADA;
        this.clinicalNotes = newAppointmentInput.observacoes();
        this.actionLogs = actionLogs;
    }

    public void updateFieldsWithNewValues(AppointmentInput updateAppointmentInput) {
        this.appointmentDateTime = updateAppointmentInput.dataHora() != null ? updateAppointmentInput.dataHora() : this.appointmentDateTime;
        this.status = updateAppointmentInput.status() != null ? AppointmentStatus.valueOf(updateAppointmentInput.status()) : this.status;
        this.clinicalNotes = updateAppointmentInput.observacoes() != null ? updateAppointmentInput.observacoes() : this.clinicalNotes;
    }

    public void updateDoctor(AppointmentInput event) {
        this.doctor = new Doctor(event);
    }

}
