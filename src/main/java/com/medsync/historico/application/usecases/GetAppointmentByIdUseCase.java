package com.medsync.historico.application.usecases;

import com.medsync.historico.application.exceptions.MedicalHistoryNotFoundException;
import com.medsync.historico.domain.entities.Appointment;
import com.medsync.historico.domain.gateways.MedicalHistoryGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class GetAppointmentByIdUseCase {

    private final MedicalHistoryGateway medicalHistoryGateway;

    public Appointment execute(String appointmentId, String patientId) {
        try {
            return medicalHistoryGateway.findAppointmentById(appointmentId, patientId);
        } catch (NoSuchElementException e) {
            throw new MedicalHistoryNotFoundException(patientId);
        }
    }

}
