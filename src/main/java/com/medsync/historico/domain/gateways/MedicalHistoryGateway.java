package com.medsync.historico.domain.gateways;

import com.medsync.historico.domain.entities.MedicalHistory;

import java.util.Optional;

public interface MedicalHistoryGateway {

    MedicalHistory save(MedicalHistory medicalHistory);
    Optional<MedicalHistory> findByPatientId(Long patientId);

}
