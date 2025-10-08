package com.medsync.historico.presentation.dto;

import com.medsync.historico.domain.entities.CreateUser;
import com.medsync.historico.domain.entities.Doctor;
import com.medsync.historico.domain.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponse {

    private String id;
    private Doctor doctor;
    private CreateUser createUser;
    private LocalDateTime appointmentDateTime;
    private AppointmentStatus status;
    private String clinicalNotes;

}
