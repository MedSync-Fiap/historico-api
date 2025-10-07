package com.medsync.historico.application.usecases;

import com.medsync.historico.application.dto.AppointmentInput;
import com.medsync.historico.domain.entities.*;
import com.medsync.historico.domain.enums.ActionType;
import com.medsync.historico.domain.gateways.MedicalHistoryGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateMedicalHistoryUseCase {

    private final MedicalHistoryGateway medicalHistoryGateway;

    public MedicalHistory execute(AppointmentInput input) {
        // Criar histórico médico apenas se o paciente já existir
        // O paciente deve ter sido criado explicitamente antes
        MedicalHistory medicalHistory = new MedicalHistory();
        
        // Buscar paciente existente pelo ID
        Patient existingPatient = medicalHistoryGateway.findPatientById(input.pacienteId())
            .orElseThrow(() -> new RuntimeException("Paciente não encontrado. Crie o paciente antes de agendar a consulta."));
        
        medicalHistory.setPatient(existingPatient);

        ActionLog actionLog = new ActionLog(input, ActionType.CREATION);

        Appointment appointment = new Appointment(input, List.of(actionLog));
        medicalHistory.setAppointments(List.of(appointment));

        return medicalHistoryGateway.save(medicalHistory);
    }

}
