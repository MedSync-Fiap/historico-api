package com.medsync.historico.presentation.controller;

import com.medsync.historico.application.services.MedicalHistoryService;
import com.medsync.historico.domain.entities.Appointment;
import com.medsync.historico.domain.entities.MedicalHistory;
import com.medsync.historico.presentation.dto.AppointmentResponse;
import com.medsync.historico.presentation.dto.MedicalHistoryResponse;
import com.medsync.historico.presentation.dto.SalvarHistoricoInput;
import com.medsync.historico.presentation.dto.AtualizarHistoricoInput;
import com.medsync.historico.presentation.mappers.AppointmentDTOMapper;
import com.medsync.historico.presentation.mappers.MedicalHistoryDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MedicalHistoryController {

    private final MedicalHistoryService medicalHistoryService;
    private final MedicalHistoryDTOMapper medicalHistoryMapper;
    private final AppointmentDTOMapper appointmentMapper;

    @QueryMapping
    public MedicalHistoryResponse getMedicalHistoryByPatientId(@Argument String patientId) {
        Long patientIdLong = Math.abs((long) patientId.hashCode());
        MedicalHistory medicalHistory = medicalHistoryService.getMedicalHistoryByPatientId(patientIdLong);
        return medicalHistoryMapper.toResponse(medicalHistory);
    }

    @QueryMapping
    public AppointmentResponse getAppointmentById(@Argument String appointmentId, @Argument String patientId) {
        Long appointmentIdLong = Math.abs((long) appointmentId.hashCode());
        Long patientIdLong = Math.abs((long) patientId.hashCode());
        Appointment appointment = medicalHistoryService.getAppointmentById(appointmentIdLong, patientIdLong);
        return appointmentMapper.toResponse(appointment);
    }

    @MutationMapping
    public MedicalHistoryResponse salvarHistorico(@Argument SalvarHistoricoInput input) {
        try {
            log.info("Salvando histórico para consulta: {}", input.consultaId());
            
            // Converter input para AppointmentEvent
            var appointmentEvent = convertToAppointmentEvent(input);
            
            // Processar baseado no tipo de evento
            MedicalHistory medicalHistory;
            if ("CRIADA".equals(input.tipoEvento())) {
                medicalHistory = medicalHistoryService.addAppointmentInMedicalHistory(appointmentEvent);
                log.info("Histórico criado/atualizado com ID: {}", medicalHistory.getId());
            } else {
                throw new IllegalArgumentException("Tipo de evento não suportado para salvar: " + input.tipoEvento());
            }
            
            return medicalHistoryMapper.toResponse(medicalHistory);
            
        } catch (Exception e) {
            log.error("Erro ao salvar histórico", e);
            throw new RuntimeException("Erro ao salvar histórico: " + e.getMessage());
        }
    }

    @MutationMapping
    public MedicalHistoryResponse atualizarHistorico(@Argument AtualizarHistoricoInput input) {
        try {
            log.info("Atualizando histórico para consulta: {}", input.consultaId());
            
            // Converter input para AppointmentEvent
            var appointmentEvent = convertToAppointmentEvent(input);
            
            // Processar baseado no tipo de evento
            MedicalHistory medicalHistory;
            if ("EDITADA".equals(input.tipoEvento())) {
                medicalHistory = medicalHistoryService.updateAppointmentInMedicalHistory(appointmentEvent);
                log.info("Histórico atualizado com ID: {}", medicalHistory.getId());
            } else {
                throw new IllegalArgumentException("Tipo de evento não suportado para atualizar: " + input.tipoEvento());
            }
            
            return medicalHistoryMapper.toResponse(medicalHistory);
            
        } catch (Exception e) {
            log.error("Erro ao atualizar histórico", e);
            throw new RuntimeException("Erro ao atualizar histórico: " + e.getMessage());
        }
    }

    private com.medsync.historico.application.dto.AppointmentEvent convertToAppointmentEvent(SalvarHistoricoInput input) {
        return new com.medsync.historico.application.dto.AppointmentEvent(
            convertUuidToLong(input.consultaId()),
            input.dataHora(),
            input.status(),
            input.observacoes(),
            input.tipoEvento(),
            input.timestamp(),
            convertUuidToLong(input.pacienteId()),
            input.pacienteNome(),
            input.pacienteCpf(),
            input.pacienteEmail(),
            input.pacienteDataNascimento(),
            convertUuidToLong(input.medicoId()),
            input.medicoNome(),
            input.medicoCpf(),
            input.medicoEmail(),
            input.medicoEspecialidade(),
            convertUuidToLong(input.usuarioId()),
            input.usuarioNome(),
            input.usuarioEmail(),
            input.usuarioRole()
        );
    }

    private com.medsync.historico.application.dto.AppointmentEvent convertToAppointmentEvent(AtualizarHistoricoInput input) {
        return new com.medsync.historico.application.dto.AppointmentEvent(
            convertUuidToLong(input.consultaId()),
            input.dataHora(),
            input.status(),
            input.observacoes(),
            input.tipoEvento(),
            input.timestamp(),
            convertUuidToLong(input.pacienteId()),
            input.pacienteNome(),
            input.pacienteCpf(),
            input.pacienteEmail(),
            input.pacienteDataNascimento(),
            convertUuidToLong(input.medicoId()),
            input.medicoNome(),
            input.medicoCpf(),
            input.medicoEmail(),
            input.medicoEspecialidade(),
            convertUuidToLong(input.usuarioId()),
            input.usuarioNome(),
            input.usuarioEmail(),
            input.usuarioRole()
        );
    }
    
    private Long convertUuidToLong(java.util.UUID uuid) {
        if (uuid == null) {
            return null;
        }
        return Math.abs((long) uuid.hashCode());
    }

}
