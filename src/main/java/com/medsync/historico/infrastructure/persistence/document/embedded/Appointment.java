package com.medsync.historico.infrastructure.persistence.document.embedded;

import com.medsync.historico.domain.enums.AppointmentStatus;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

public record Appointment(
        Long id,
        AppointmentStatus status,
        DoctorInfo doctor,
        @Field("created_by") CreateUserInfo createdBy,
        @Field("appointment_date") LocalDateTime appointmentDate,
        @Field("action_logs") List<ActionLog> actionLogs
) {}
