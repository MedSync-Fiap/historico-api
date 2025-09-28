package com.medsync.historico.application.dto;

import com.medsync.historico.domain.enums.EventType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AppointmentEvent (
        Long consultaId,
        LocalDateTime dataHora,
        String status,
        String observacoes,
        String tipoEvento,
        LocalDateTime timestamp,

        Long pacienteId,
        String pacienteNome,
        String pacienteCpf,
        String pacienteEmail,
        LocalDate pacienteDataNascimento,

        Long medicoId,
        String medicoNome,
        String medicoCpf,
        String medicoEmail,
        String medicoEspecialidade,

        Long usuarioId,
        String usuarioNome,
        String usuarioEmail,
        String usuarioRole
) {
}
