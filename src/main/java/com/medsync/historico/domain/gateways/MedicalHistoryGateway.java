package com.medsync.historico.domain.gateways;

import com.medsync.historico.domain.entities.MedicalHistory;

import java.util.Optional;

public interface MedicalHistoryGateway {

    MedicalHistory create(MedicalHistory medicalHistory);
    MedicalHistory update(MedicalHistory medicalHistory);
    Optional<MedicalHistory> findByPatientId(String patientId);

}
