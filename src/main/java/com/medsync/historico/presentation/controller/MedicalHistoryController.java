package com.medsync.historico.presentation.controller;

import com.medsync.historico.application.dto.AppointmentInput;
import com.medsync.historico.application.services.MedicalHistoryService;
import com.medsync.historico.domain.entities.Appointment;
import com.medsync.historico.domain.entities.MedicalHistory;
import com.medsync.historico.domain.enums.EventType;
import com.medsync.historico.presentation.dto.AppointmentFilterInput;
import com.medsync.historico.presentation.dto.AppointmentResponse;
import com.medsync.historico.presentation.dto.MedicalHistoryResponse;
import com.medsync.historico.presentation.mappers.AppointmentDTOMapper;
import com.medsync.historico.presentation.mappers.MedicalHistoryDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MedicalHistoryController {

    private final MedicalHistoryService medicalHistoryService;
    private final MedicalHistoryDTOMapper medicalHistoryMapper;
    private final AppointmentDTOMapper appointmentMapper;

    @QueryMapping
    public MedicalHistoryResponse getMedicalHistoryByPatientId(@Argument String patientId) {
        MedicalHistory medicalHistory = medicalHistoryService.getMedicalHistoryByPatientId(patientId);
        return medicalHistoryMapper.toResponse(medicalHistory);
    }

    @QueryMapping
    public AppointmentResponse getAppointmentById(@Argument String appointmentId, @Argument String patientId) {
        Appointment appointment = medicalHistoryService.getAppointmentById(appointmentId, patientId);
        return appointmentMapper.toResponse(appointment);
    }

    @MutationMapping
    public MedicalHistoryResponse saveNewAppointment(@Argument AppointmentInput newAppointmentInput) {

        if (EventType.CREATION.getValue().equals(newAppointmentInput.tipoEvento())) {

            MedicalHistory medicalHistory = medicalHistoryService.addAppointmentInMedicalHistory(newAppointmentInput);
            log.info("New appointment added in medical history with ID: {}", medicalHistory.getId());

            return medicalHistoryMapper.toResponse(medicalHistory);

        } else {
            throw new IllegalArgumentException("Unsupported event type for creation: " + newAppointmentInput.tipoEvento());
        }

    }

    @MutationMapping
    public MedicalHistoryResponse updateAppointment(@Argument AppointmentInput updateAppointmentInput) {

        EventType eventType = EventType.fromValue(updateAppointmentInput.tipoEvento());

        if (eventType != null && eventType.equals(EventType.EDITION)) {
            MedicalHistory medicalHistory = medicalHistoryService.updateAppointmentInMedicalHistory(updateAppointmentInput);
            log.info("Medical history updated with ID: {}", medicalHistory.getId());

            return medicalHistoryMapper.toResponse(medicalHistory);

        } else {
            throw new IllegalArgumentException("Unsupported event type for update: " + updateAppointmentInput.tipoEvento());
        }

    }

    @SchemaMapping(typeName = "MedicalHistoryResponse", field = "appointments")
    public List<Appointment> getAppointments(MedicalHistoryResponse history, @Argument AppointmentFilterInput filter) {
        List<Appointment> allAppointments = history.getAppointments();

        if (filter == null || filter.onlyFuture() == null || Boolean.FALSE.equals(filter.onlyFuture())) {
            return allAppointments;
        }

        final LocalDateTime now = LocalDateTime.now();
        return allAppointments.stream()
                .filter(appointment -> appointment.getAppointmentDateTime().isAfter(now))
                .toList();
    }

}