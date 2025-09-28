package com.medsync.historico.infrastructure.persistence.repositories;

import com.medsync.historico.application.exceptions.AppointmentNotFoundException;
import com.medsync.historico.domain.entities.Appointment;
import com.medsync.historico.domain.entities.MedicalHistory;
import com.medsync.historico.domain.gateways.MedicalHistoryGateway;
import com.medsync.historico.infrastructure.persistence.document.MedicalHistoryDocument;
import com.medsync.historico.infrastructure.persistence.mappers.AppointmentMapper;
import com.medsync.historico.infrastructure.persistence.mappers.MedicalHistoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MedicalHistoryRepositoryImpl implements MedicalHistoryGateway {

    private final MedicalHistoryMongoRepository repository;
    private final MedicalHistoryMapper medicalHistoryMapper;
    private final AppointmentMapper appointmentMapper;

    @Override
    public MedicalHistory save(MedicalHistory medicalHistory) {
        MedicalHistoryDocument document = medicalHistoryMapper.toDocument(medicalHistory);
        MedicalHistoryDocument savedDocument = repository.save(document);
        return medicalHistoryMapper.toDomain(savedDocument);
    }

    @Override
    public Optional<MedicalHistory> findByPatientId(Long patientId) {
        return repository.findByPatientId(patientId)
                .map(medicalHistoryMapper::toDomain);
    }

    @Override
    public Appointment findAppointmentById(Long appointmentId, Long patientId) {
        MedicalHistoryDocument medicalHistory = repository.findByPatientId(patientId)
                .orElseThrow();

        return medicalHistory.getAppointments().stream()
                .filter(appointment -> appointment.id().equals(appointmentId))
                .findFirst()
                .map(appointmentMapper::toDomain)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentId));
    }
}
