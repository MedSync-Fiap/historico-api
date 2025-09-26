package com.medsync.historico.presentation.dto;

import com.medsync.historico.domain.entities.Appointment;
import com.medsync.historico.domain.entities.Patient;
import lombok.Data;

import java.util.List;

@Data
public class MedicalHistoryResponse {

    private Patient patient;
    private List<Appointment> appointments;

}
