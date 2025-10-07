package com.medsync.historico.application.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AppointmentInput(
        String consultaId,
        LocalDateTime dataHora,
        String status,
        String observacoes,
        String tipoEvento,
        LocalDateTime timestamp,

        // Apenas ID do paciente - o historico-api validará se existe
        String pacienteId,
        
        // Dados completos do médico
        String medicoId,
        String medicoNome,
        String medicoCpf,
        String medicoEmail,
        
        // Dados completos da especialidade
        String especialidadeId,
        String especialidadeNome,
        
        // Dados completos do usuário
        String usuarioId,
        String usuarioNome,
        String usuarioEmail,
        String usuarioRole
) {
}
