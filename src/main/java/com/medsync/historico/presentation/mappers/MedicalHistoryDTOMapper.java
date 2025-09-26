package com.medsync.historico.presentation.mappers;

import com.medsync.historico.domain.entities.MedicalHistory;
import com.medsync.historico.presentation.dto.MedicalHistoryResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicalHistoryDTOMapper {

    MedicalHistoryResponse toResponse(MedicalHistory medicalHistory);

}
