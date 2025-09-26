package com.medsync.historico.domain.entities;

import com.medsync.historico.application.dto.AppointmentEvent;
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

    private Long id;
    private Doctor doctor;
    private CreateUser createUser;
    private LocalDateTime appointmentDate;
    private AppointmentStatus status;
    private String clinicalNotes;
    private List<ActionLog> actionLogs;

    public Appointment(AppointmentEvent event, List<ActionLog> actionLogs) {
        this.id = event.consultaId();
        this.doctor = new Doctor(event);
        this.createUser = new CreateUser(event);
        this.appointmentDate = event.dataHora();
        this.status = AppointmentStatus.AGENDADA;
        this.clinicalNotes = event.observacoes();
        this.actionLogs = actionLogs;
    }
}
