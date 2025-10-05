package com.medsync.historico.application.services;

import com.medsync.historico.application.dto.AppointmentInput;
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

    public MedicalHistory createMedicalHistory(AppointmentInput input) {
        return createMedicalHistoryUseCase.execute(input);
    }

    public MedicalHistory addAppointmentInMedicalHistory(AppointmentInput newAppointmentInput) {
        try {
            MedicalHistory existingHistory = getMedicalHistoryByPatientId(newAppointmentInput.pacienteId());
            return saveNewAppointmentUseCase.execute(newAppointmentInput, existingHistory);
        } catch (MedicalHistoryNotFoundException e) {
            return createMedicalHistory(newAppointmentInput);
        }
    }

    public MedicalHistory updateAppointmentInMedicalHistory(AppointmentInput updateAppointmentInput) {
        MedicalHistory existingHistory = getMedicalHistoryByPatientId(updateAppointmentInput.pacienteId());
        return updateAppointmentUseCase.execute(updateAppointmentInput, existingHistory);
    }

    public MedicalHistory getMedicalHistoryByPatientId(String patientId) {
        return getMedicalHistoryByPatientIdUseCase.execute(patientId);
    }

    public Appointment getAppointmentById(String appointmentId, String patientId) {
        return getAppointmentByIdUseCase.execute(appointmentId, patientId);
    }

    public List<MedicalHistory> getAllMedicalHistories() {
        return medicalHistoryGateway.findAll();
    }

}
