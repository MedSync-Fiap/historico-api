package com.medsync.historico.infrastructure.persistence.repositories;

import com.medsync.historico.infrastructure.persistence.document.MedicalHistoryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicalHistoryMongoRepository extends MongoRepository<MedicalHistoryDocument, String> {
    Optional<MedicalHistoryDocument> findByPatientId(String patientId);
    Optional<MedicalHistoryDocument> findFirstByPatientCpf(String patientCpf);
}
