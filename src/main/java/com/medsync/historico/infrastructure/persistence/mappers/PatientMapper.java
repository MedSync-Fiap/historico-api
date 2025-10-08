package com.medsync.historico.infrastructure.persistence.mappers;

import com.medsync.historico.domain.entities.Patient;
import com.medsync.historico.infrastructure.persistence.document.embedded.PatientDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    @Mapping(target = "nome", source = "name")
    @Mapping(target = "dataNascimento", source = "dateOfBirth", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "ativo", source = "ativo")
    Patient toDomain(PatientDocument document);

    @Mapping(target = "name", source = "nome")
    @Mapping(target = "dateOfBirth", source = "dataNascimento", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "ativo", source = "ativo")
    PatientDocument toDocument(Patient patient);

}
