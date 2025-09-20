package com.medsync.historico.domain.entities;

import com.medsync.historico.domain.enums.AppointmentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Appointment {

    private String id;
    private Doctor doctor;
    private CreateUser createUser;
    private LocalDateTime appointmentDate;
    private AppointmentStatus status;

}
