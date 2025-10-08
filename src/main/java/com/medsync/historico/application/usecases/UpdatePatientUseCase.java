package com.medsync.historico.application.usecases;

import com.medsync.historico.application.exceptions.MedicalHistoryNotFoundException;
import com.medsync.historico.domain.gateways.MedicalHistoryGateway;
import com.medsync.historico.presentation.dto.PatientInput;
import com.medsync.historico.presentation.dto.PatientResponse;
import com.medsync.historico.presentation.mappers.PatientDtoMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class UpdatePatientUseCase {
    
    private final MedicalHistoryGateway medicalHistoryGateway;
    private final PatientDtoMapper patientDtoMapper;
    
    public UpdatePatientUseCase(MedicalHistoryGateway medicalHistoryGateway, PatientDtoMapper patientDtoMapper) {
        this.medicalHistoryGateway = medicalHistoryGateway;
        this.patientDtoMapper = patientDtoMapper;
    }
    
    public PatientResponse execute(String patientId, PatientInput patientInput) {

        var medicalHistory = medicalHistoryGateway.findByPatientId(patientId)
                .orElseThrow(() -> new MedicalHistoryNotFoundException("Paciente não encontrado com ID: " + patientId));
        
        var patient = medicalHistory.getPatient();
        if (patient == null) {
            throw new MedicalHistoryNotFoundException("Dados do paciente não encontrados no histórico");
        }
        
        if (patientInput.nome() != null && !patientInput.nome().trim().isEmpty()) {
            patient.setNome(patientInput.nome());
        }
        
        if (patientInput.cpf() != null && !patientInput.cpf().trim().isEmpty()) {
            patient.setCpf(patientInput.cpf());
        }
        
        if (patientInput.email() != null && !patientInput.email().trim().isEmpty()) {
            patient.setEmail(patientInput.email());
        }
        
        if (patientInput.dataNascimento() != null && !patientInput.dataNascimento().trim().isEmpty()) {
            patient.setDataNascimento(patientInput.dataNascimento());
        }
        
        if (patientInput.observacoes() != null) {
            patient.setObservacoes(patientInput.observacoes());
        }
        
        // Garantir que o campo ativo seja preservado (não pode ser null)
        if (patient.getAtivo() == null) {
            patient.setAtivo(true);
        }
        
        patient.setAtualizadoEm(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        medicalHistoryGateway.save(medicalHistory);
        
        return patientDtoMapper.toResponse(patient);
    }
}
