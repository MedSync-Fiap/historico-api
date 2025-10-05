package com.medsync.historico.application.usecases;

import com.medsync.historico.application.dto.AppointmentInput;
import com.medsync.historico.domain.entities.ActionLog;
import com.medsync.historico.domain.entities.Appointment;
import com.medsync.historico.domain.entities.MedicalHistory;
import com.medsync.historico.domain.enums.ActionType;
import com.medsync.historico.domain.gateways.MedicalHistoryGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SaveNewAppointmentUseCase {

    private final MedicalHistoryGateway medicalHistoryGateway;

    public MedicalHistory execute(AppointmentInput newAppointmentInput, MedicalHistory medicalHistory) {
        ActionLog actionLog = new ActionLog(newAppointmentInput, ActionType.CREATION);

        Appointment appointment = new Appointment(newAppointmentInput, List.of(actionLog));

        if (medicalHistory.getAppointments() != null) {
           medicalHistory.getAppointments().add(appointment);
        } else {
            medicalHistory.setAppointments(List.of(appointment));
        }

        return medicalHistoryGateway.save(medicalHistory);
    }

}
