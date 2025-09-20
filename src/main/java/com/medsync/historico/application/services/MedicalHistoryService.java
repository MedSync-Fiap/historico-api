package com.medsync.historico.application.services;

import com.medsync.historico.application.usecases.SearchMedicalHistoryByPatientIdUseCase;
import com.medsync.historico.domain.entities.MedicalHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicalHistoryService {

    private final SearchMedicalHistoryByPatientIdUseCase searchMedicalHistoryByPatientIdUseCase;

    public MedicalHistory getMedicalHistoryByPatientId(String patientId) {
        return searchMedicalHistoryByPatientIdUseCase.execute(patientId);
    }

}
