package com.medsync.historico.infrastructure.persistence.mappers;

import com.medsync.historico.domain.entities.MedicalHistory;
import com.medsync.historico.infrastructure.persistence.document.MedicalHistoryDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PatientMapper.class, AppointmentMapper.class})
public interface MedicalHistoryMapper {

    MedicalHistory toDomain(MedicalHistoryDocument document);

    MedicalHistoryDocument toDocument(MedicalHistory medicalHistory);

}
