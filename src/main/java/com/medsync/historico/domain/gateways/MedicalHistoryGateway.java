package com.medsync.historico.domain.gateways;

import com.medsync.historico.domain.entities.Appointment;
import com.medsync.historico.domain.entities.MedicalHistory;
import com.medsync.historico.domain.entities.Patient;

import java.util.List;
import java.util.Optional;

public interface MedicalHistoryGateway {

    MedicalHistory save(MedicalHistory medicalHistory);
    Optional<MedicalHistory> findByPatientId(String patientId);
    Optional<MedicalHistory> findByPatientCpf(String patientCpf);
    Appointment findAppointmentById(String appointmentId, String patientId);
    List<MedicalHistory> findAll();
    Optional<Patient> findPatientById(String patientId);

}
