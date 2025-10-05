package com.medsync.historico.infrastructure.persistence.document.embedded;

import com.medsync.historico.domain.enums.AppointmentStatus;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

public record AppointmentDocument(
        String id,
        AppointmentStatus status,
        DoctorDocument doctor,
        @Field("created_by") CreateUserDocument createdBy,
        @Field("appointment_datetime") LocalDateTime appointmentDateTime,
        @Field("action_logs") List<ActionLogDocument> actionLogs
) {}
