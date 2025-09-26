package com.medsync.historico.application.usecases;

import com.medsync.historico.application.exceptions.MedicalHistoryNotFoundException;
import com.medsync.historico.domain.entities.MedicalHistory;
import com.medsync.historico.domain.gateways.MedicalHistoryGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SearchMedicalHistoryByPatientIdUseCase {

    private final MedicalHistoryGateway medicalHistoryGateway;

    public MedicalHistory execute(Long patientId) {
        Optional<MedicalHistory> medicalHistoryOpt = medicalHistoryGateway.findByPatientId(patientId);
        if (medicalHistoryOpt.isEmpty()) {
            throw new MedicalHistoryNotFoundException(patientId);
        }
        return medicalHistoryOpt.get();
    }

}
