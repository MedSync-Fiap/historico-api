package com.medsync.historico.presentation.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record AtualizarHistoricoInput(
    UUID consultaId,
    LocalDateTime dataHora,
    String status,
    String observacoes,
    String tipoEvento,
    LocalDateTime timestamp,
    
    // Dados do paciente
    UUID pacienteId,
    String pacienteNome,
    String pacienteCpf,
    String pacienteEmail,
    LocalDate pacienteDataNascimento,
    
    // Dados do médico
    UUID medicoId,
    String medicoNome,
    String medicoCpf,
    String medicoEmail,
    String medicoEspecialidade,
    
    // Dados do usuário
    UUID usuarioId,
    String usuarioNome,
    String usuarioEmail,
    String usuarioRole
) {}
