package com.medsync.historico.application.services;

import com.medsync.historico.application.dto.AppointmentEvent;
import com.medsync.historico.application.exceptions.MedicalHistoryNotFoundException;
import com.medsync.historico.application.usecases.*;
import com.medsync.historico.domain.entities.Appointment;
import com.medsync.historico.domain.entities.MedicalHistory;
import com.medsync.historico.domain.gateways.MedicalHistoryGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalHistoryService {

    private final GetMedicalHistoryByPatientIdUseCase getMedicalHistoryByPatientIdUseCase;
    private final SaveNewAppointmentUseCase saveNewAppointmentUseCase;
    private final CreateMedicalHistoryUseCase createMedicalHistoryUseCase;
    private final UpdateAppointmentUseCase updateAppointmentUseCase;
    private final GetAppointmentByIdUseCase getAppointmentByIdUseCase;
    private final MedicalHistoryGateway medicalHistoryGateway;

    public MedicalHistory createMedicalHistory(AppointmentEvent event) {
        return createMedicalHistoryUseCase.execute(event);
    }

    public MedicalHistory addAppointmentInMedicalHistory(AppointmentEvent event) {
        try {
            MedicalHistory existingHistory = getMedicalHistoryByPatientId(event.pacienteId());
            return saveNewAppointmentUseCase.execute(event, existingHistory);
        } catch (MedicalHistoryNotFoundException e) {
            return createMedicalHistory(event);
        }
    }

    public MedicalHistory updateAppointmentInMedicalHistory(AppointmentEvent event) {
        MedicalHistory existingHistory = getMedicalHistoryByPatientId(event.pacienteId());
        return updateAppointmentUseCase.execute(event, existingHistory);
    }

    public MedicalHistory getMedicalHistoryByPatientId(Long patientId) {
        return getMedicalHistoryByPatientIdUseCase.execute(patientId);
    }

    public Appointment getAppointmentById(Long appointmentId, Long patientId) {
        return getAppointmentByIdUseCase.execute(appointmentId, patientId);
    }

    public List<MedicalHistory> getAllMedicalHistories() {
        return medicalHistoryGateway.findAll();
    }

}
