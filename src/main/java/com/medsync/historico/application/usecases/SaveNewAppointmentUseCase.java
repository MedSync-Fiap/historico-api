package com.medsync.historico.application.usecases;

import com.medsync.historico.application.dto.AppointmentEvent;
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

    public MedicalHistory execute(AppointmentEvent event, MedicalHistory medicalHistory) {
        ActionLog actionLog = new ActionLog(event, ActionType.CREATION);

        Appointment appointment = new Appointment(event, List.of(actionLog));

        if (medicalHistory.getAppointments() != null) {
           medicalHistory.getAppointments().add(appointment);
        } else {
            medicalHistory.setAppointments(List.of(appointment));
        }

        return medicalHistoryGateway.save(medicalHistory);
    }

}
