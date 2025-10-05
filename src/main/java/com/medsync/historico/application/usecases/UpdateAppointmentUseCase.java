package com.medsync.historico.application.usecases;

import com.medsync.historico.application.dto.AppointmentInput;
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

    public MedicalHistory execute(AppointmentInput updateAppointmentInput, MedicalHistory medicalHistory) {

        ActionLog actionLog = new ActionLog(updateAppointmentInput, ActionType.EDITION);

        Appointment appointmentToUpdate = medicalHistory.getAppointments().stream()
                .filter(appointment -> appointment.getId().equals(updateAppointmentInput.consultaId()))
                .findFirst()
                .orElseThrow(() -> new AppointmentNotFoundException(updateAppointmentInput.consultaId()));

        appointmentToUpdate.updateFieldsWithNewValues(updateAppointmentInput);
        if (!appointmentToUpdate.getDoctor().getId().equals(updateAppointmentInput.medicoId())) {
            appointmentToUpdate.updateDoctor(updateAppointmentInput);
        }

        appointmentToUpdate.getActionLogs().add(actionLog);

        return medicalHistoryGateway.save(medicalHistory);
    }

}
