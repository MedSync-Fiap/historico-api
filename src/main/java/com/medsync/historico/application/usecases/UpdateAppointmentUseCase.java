package com.medsync.historico.application.usecases;

import com.medsync.historico.application.dto.AppointmentEvent;
import com.medsync.historico.application.exceptions.AppointmentNotFoundException;
import com.medsync.historico.domain.entities.ActionLog;
import com.medsync.historico.domain.entities.Appointment;
import com.medsync.historico.domain.entities.MedicalHistory;
import com.medsync.historico.domain.enums.ActionType;
import com.medsync.historico.domain.gateways.MedicalHistoryGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateAppointmentUseCase {

    private final MedicalHistoryGateway medicalHistoryGateway;

    public MedicalHistory execute(AppointmentEvent event, MedicalHistory medicalHistory) {

        ActionLog actionLog = new ActionLog(event, ActionType.EDITION);

        Appointment appointmentToUpdate = medicalHistory.getAppointments().stream()
                .filter(appointment -> appointment.getId().equals(event.consultaId()))
                .findFirst()
                .orElseThrow(() -> new AppointmentNotFoundException(event.consultaId()));

        appointmentToUpdate.updateFieldsWithNewValues(event);
        if (!appointmentToUpdate.getDoctor().getId().equals(event.medicoId())) {
            appointmentToUpdate.updateDoctor(event);
        }

        appointmentToUpdate.getActionLogs().add(actionLog);

        return medicalHistoryGateway.save(medicalHistory);
    }

}
