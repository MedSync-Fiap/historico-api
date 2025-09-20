package com.medsync.historico.infrastructure.persistence.repositories;

import com.medsync.historico.domain.entities.MedicalHistory;
import com.medsync.historico.domain.gateways.MedicalHistoryGateway;
import com.medsync.historico.presentation.mappers.MedicalHistoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MedicalHistoryRepositoryImpl implements MedicalHistoryGateway {

    private final MedicalHistoryMongoRepository repository;
    private final MedicalHistoryMapper mapper;

    @Override
    public MedicalHistory create(MedicalHistory medicalHistory) {
        return null;
    }

    @Override
    public MedicalHistory update(MedicalHistory medicalHistory) {
        return null;
    }

    @Override
    public Optional<MedicalHistory> findByPatientId(String patientId) {
        return repository.findByPatientId(patientId)
                .map(mapper::toDomain);
    }
}
