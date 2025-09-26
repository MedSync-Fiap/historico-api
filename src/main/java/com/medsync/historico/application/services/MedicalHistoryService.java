package com.medsync.historico.application.services;

import com.medsync.historico.application.dto.AppointmentEvent;
import com.medsync.historico.application.exceptions.MedicalHistoryNotFoundException;
import com.medsync.historico.application.usecases.CreateMedicalHistoryUseCase;
import com.medsync.historico.application.usecases.SaveNewAppointmentUseCase;
import com.medsync.historico.application.usecases.SearchMedicalHistoryByPatientIdUseCase;
import com.medsync.historico.domain.entities.MedicalHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicalHistoryService {

    private final SearchMedicalHistoryByPatientIdUseCase searchMedicalHistoryByPatientIdUseCase;
    private final SaveNewAppointmentUseCase saveNewAppointmentUseCase;
    private final CreateMedicalHistoryUseCase createMedicalHistoryUseCase;

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

    public MedicalHistory getMedicalHistoryByPatientId(Long patientId) {
        return searchMedicalHistoryByPatientIdUseCase.execute(patientId);
    }

}
