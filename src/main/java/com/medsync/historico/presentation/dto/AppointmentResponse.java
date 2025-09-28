package com.medsync.historico.presentation.dto;

import com.medsync.historico.domain.entities.CreateUser;
import com.medsync.historico.domain.entities.Doctor;
import com.medsync.historico.domain.enums.AppointmentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentResponse {

    private Long id;
    private Doctor doctor;
    private CreateUser createUser;
    private LocalDateTime appointmentDateTime;
    private AppointmentStatus status;
    private String clinicalNotes;

}
