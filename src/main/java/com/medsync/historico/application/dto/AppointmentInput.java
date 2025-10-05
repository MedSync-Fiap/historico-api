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

        String pacienteId,
        String pacienteNome,
        String pacienteCpf,
        String pacienteEmail,
        LocalDate pacienteDataNascimento,

        String medicoId,
        String medicoNome,
        String medicoCpf,
        String medicoEmail,
        String medicoEspecialidade,

        String usuarioId,
        String usuarioNome,
        String usuarioEmail,
        String usuarioRole
) {
}
