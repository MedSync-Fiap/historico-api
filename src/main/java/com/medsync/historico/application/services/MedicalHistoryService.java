package com.medsync.historico.application.services;

import com.medsync.historico.application.dto.AppointmentInput;
import com.medsync.historico.domain.entities.Patient;
import com.medsync.historico.presentation.dto.PatientInput;
import com.medsync.historico.presentation.dto.PatientResponse;
import com.medsync.historico.application.exceptions.MedicalHistoryNotFoundException;
import com.medsync.historico.application.usecases.*;
import com.medsync.historico.domain.entities.Appointment;
import com.medsync.historico.domain.entities.MedicalHistory;
import com.medsync.historico.domain.gateways.MedicalHistoryGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalHistoryService {

    private final GetMedicalHistoryByPatientIdUseCase getMedicalHistoryByPatientIdUseCase;
    private final SaveNewAppointmentUseCase saveNewAppointmentUseCase;
    private final CreateMedicalHistoryUseCase createMedicalHistoryUseCase;
    private final UpdateAppointmentUseCase updateAppointmentUseCase;
    private final GetAppointmentByIdUseCase getAppointmentByIdUseCase;
    private final MedicalHistoryGateway medicalHistoryGateway;

    public MedicalHistory createMedicalHistory(AppointmentInput input) {
        return createMedicalHistoryUseCase.execute(input);
    }

    public MedicalHistory addAppointmentInMedicalHistory(AppointmentInput newAppointmentInput) {
        try {
            MedicalHistory existingHistory = getMedicalHistoryByPatientId(newAppointmentInput.pacienteId());
            return saveNewAppointmentUseCase.execute(newAppointmentInput, existingHistory);
        } catch (MedicalHistoryNotFoundException e) {
            return createMedicalHistory(newAppointmentInput);
        }
    }

    public MedicalHistory updateAppointmentInMedicalHistory(AppointmentInput updateAppointmentInput) {
        MedicalHistory existingHistory = getMedicalHistoryByPatientId(updateAppointmentInput.pacienteId());
        return updateAppointmentUseCase.execute(updateAppointmentInput, existingHistory);
    }

    public MedicalHistory getMedicalHistoryByPatientId(String patientId) {
        return getMedicalHistoryByPatientIdUseCase.execute(patientId);
    }

    public MedicalHistory getMedicalHistoryByPatientCpf(String patientCpf) {
        return medicalHistoryGateway.findByPatientCpf(patientCpf)
                .orElseThrow(() -> new MedicalHistoryNotFoundException("Histórico médico não encontrado para o CPF: " + patientCpf));
    }

    public Appointment getAppointmentById(String appointmentId, String patientId) {
        return getAppointmentByIdUseCase.execute(appointmentId, patientId);
    }

    public List<MedicalHistory> getAllMedicalHistories() {
        return medicalHistoryGateway.findAll();
    }

    public PatientResponse createPatient(PatientInput patientInput) {
        // Criar entidade Patient
        Patient patient = new Patient();
        patient.setId(java.util.UUID.randomUUID().toString());
        patient.setName(patientInput.nome());
        patient.setCpf(patientInput.cpf());
        patient.setEmail(patientInput.email());
        patient.setDateOfBirth(java.time.LocalDate.parse(patientInput.dataNascimento()));
        
        // Criar histórico médico vazio para o paciente
        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setPatient(patient);
        medicalHistory.setAppointments(java.util.Collections.emptyList());
        
        // Salvar no banco
        MedicalHistory savedHistory = medicalHistoryGateway.save(medicalHistory);
        Patient savedPatient = savedHistory.getPatient();
        
        return new PatientResponse(
            savedPatient.getId(),
            savedPatient.getName(),
            savedPatient.getCpf(),
            savedPatient.getEmail(),
            savedPatient.getDateOfBirth().toString(),
            patientInput.observacoes(),
            true,
            java.time.LocalDateTime.now()
        );
    }

}
