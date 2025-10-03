package com.medsync.historico.domain.gateways;

import com.medsync.historico.domain.entities.Appointment;
import com.medsync.historico.domain.entities.MedicalHistory;

import java.util.List;
import java.util.Optional;

public interface MedicalHistoryGateway {

    MedicalHistory save(MedicalHistory medicalHistory);
    Optional<MedicalHistory> findByPatientId(Long patientId);
    Appointment findAppointmentById(Long appointmentId, Long patientId);
    List<MedicalHistory> findAll();

}
