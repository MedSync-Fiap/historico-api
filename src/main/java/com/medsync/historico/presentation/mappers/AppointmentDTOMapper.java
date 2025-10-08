package com.medsync.historico.presentation.mappers;

import com.medsync.historico.domain.entities.Appointment;
import com.medsync.historico.domain.entities.Patient;
import com.medsync.historico.presentation.dto.AppointmentResponse;
import com.medsync.historico.presentation.dto.PatientResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentDTOMapper {

    AppointmentResponse toResponse(Appointment appointment);
}
