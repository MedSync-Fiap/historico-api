package com.medsync.historico.infrastructure.persistence.mappers;

import com.medsync.historico.domain.entities.Appointment;
import com.medsync.historico.infrastructure.persistence.document.embedded.AppointmentDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    Appointment toDomain(AppointmentDocument document);

}
