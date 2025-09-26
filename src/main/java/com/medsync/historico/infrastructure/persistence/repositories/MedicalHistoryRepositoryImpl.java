package com.medsync.historico.infrastructure.persistence.repositories;

import com.medsync.historico.domain.entities.MedicalHistory;
import com.medsync.historico.domain.gateways.MedicalHistoryGateway;
import com.medsync.historico.infrastructure.persistence.document.MedicalHistoryDocument;
import com.medsync.historico.infrastructure.persistence.mappers.MedicalHistoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MedicalHistoryRepositoryImpl implements MedicalHistoryGateway {

    private final MedicalHistoryMongoRepository repository;
    private final MedicalHistoryMapper mapper;

    @Override
    public MedicalHistory save(MedicalHistory medicalHistory) {
        MedicalHistoryDocument document = mapper.toDocument(medicalHistory);
        MedicalHistoryDocument savedDocument = repository.save(document);
        return mapper.toDomain(savedDocument);
    }

    @Override
    public MedicalHistory update(MedicalHistory medicalHistory) {
        return null;
    }

    @Override
    public Optional<MedicalHistory> findByPatientId(Long patientId) {
        return repository.findByPatientId(patientId)
                .map(mapper::toDomain);
    }
}
