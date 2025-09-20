package com.medsync.historico.infrastructure.persistence.document.embedded;

import com.medsync.historico.domain.enums.AppointmentStatus;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

public record Appointment(
        String id,
        DoctorInfo doctor,
        @Field("created_by") CreateUserInfo createdBy,
        @Field("date_time") LocalDateTime dateTime,
        AppointmentStatus status,
        @Field("actionLogs") List<ActionLog> actionLogs
) {}
