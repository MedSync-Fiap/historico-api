package com.medsync.historico.presentation.controller;

import com.medsync.historico.application.services.MedicalHistoryService;
import com.medsync.historico.domain.entities.Appointment;
import com.medsync.historico.domain.entities.MedicalHistory;
import com.medsync.historico.presentation.dto.AppointmentResponse;
import com.medsync.historico.presentation.dto.MedicalHistoryResponse;
import com.medsync.historico.presentation.mappers.AppointmentDTOMapper;
import com.medsync.historico.presentation.mappers.MedicalHistoryDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MedicalHistoryController {

    private final MedicalHistoryService medicalHistoryService;
    private final MedicalHistoryDTOMapper medicalHistoryMapper;
    private final AppointmentDTOMapper appointmentMapper;

    @QueryMapping
    public MedicalHistoryResponse getMedicalHistoryByPatientId(@Argument Long patientId) {
        MedicalHistory medicalHistory = medicalHistoryService.getMedicalHistoryByPatientId(patientId);
        return medicalHistoryMapper.toResponse(medicalHistory);
    }

    @QueryMapping
    public AppointmentResponse getAppointmentById(@Argument Long appointmentId, @Argument Long patientId) {
        Appointment appointment = medicalHistoryService.getAppointmentById(appointmentId, patientId);
        return appointmentMapper.toResponse(appointment);
    }

}
