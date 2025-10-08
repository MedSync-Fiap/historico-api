package com.medsync.historico.presentation.mappers;

import com.medsync.historico.domain.entities.Patient;
import com.medsync.historico.presentation.dto.PatientResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientDtoMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "cpf", source = "cpf")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "dataNascimento", source = "dataNascimento")
    @Mapping(target = "observacoes", source = "observacoes")
    @Mapping(target = "ativo", source = "ativo")
    @Mapping(target = "criadoEm", source = "criadoEm")
    @Mapping(target = "atualizadoEm", source = "atualizadoEm")
    PatientResponse toResponse(Patient patient);

}
