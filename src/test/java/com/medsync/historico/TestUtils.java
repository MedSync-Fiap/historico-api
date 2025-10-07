package com.medsync.historico;

import com.medsync.historico.application.dto.AppointmentInput;
import com.medsync.historico.domain.entities.*;
import com.medsync.historico.domain.enums.ActionType;
import com.medsync.historico.domain.enums.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public abstract class TestUtils {

    public static final String APPOINTMENT_ID = "850e8400-e29b-41d4-a716-446655440225";
    public static final String USER_ID = "980e8400-e29b-41d4-a716-446655440001";
    public static final String PATIENT_ID = "850e8400-e29b-41d4-a716-446655440004";

    public static Map<String, Object> convertAppointmentInputToMap(AppointmentInput appointmentInput) {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put("consultaId", appointmentInput.consultaId());
        map.put("dataHora", appointmentInput.dataHora().toString());
        map.put("status", appointmentInput.status());
        map.put("observacoes", appointmentInput.observacoes());
        map.put("tipoEvento", appointmentInput.tipoEvento());
        map.put("timestamp", appointmentInput.timestamp().toString());

        map.put("pacienteId", appointmentInput.pacienteId());
        map.put("pacienteNome", appointmentInput.pacienteNome());
        map.put("pacienteCpf", appointmentInput.pacienteCpf());
        map.put("pacienteEmail", appointmentInput.pacienteEmail());
        map.put("pacienteDataNascimento", appointmentInput.pacienteDataNascimento().toString());

        map.put("medicoId", appointmentInput.medicoId());
        map.put("medicoNome", appointmentInput.medicoNome());
        map.put("medicoCpf", appointmentInput.medicoCpf());
        map.put("medicoEmail", appointmentInput.medicoEmail());
        map.put("medicoEspecialidade", appointmentInput.medicoEspecialidade());

        map.put("usuarioId", appointmentInput.usuarioId());
        map.put("usuarioNome", appointmentInput.usuarioNome());
        map.put("usuarioEmail", appointmentInput.usuarioEmail());
        map.put("usuarioRole", appointmentInput.usuarioRole());

        return map;
    }

    public static Appointment getAppointment(boolean isPast) {
        Appointment appointment = new Appointment();
        appointment.setId(isPast? UUID.randomUUID().toString() : APPOINTMENT_ID);
        appointment.setAppointmentDateTime(isPast? LocalDateTime.now().minusDays(10) : LocalDateTime.now().plusDays(3));
        appointment.setStatus(AppointmentStatus.AGENDADA);
        appointment.setDoctor(getDoctor());
        appointment.setCreateUser(getCreateUser());
        appointment.setClinicalNotes("Paciente apresenta sintomas de dor no peito.");
        appointment.setActionLogs(Collections.singletonList(getActionLog(getCreateUser())));
        return appointment;
    }

    public static Patient getPatient() {
        Patient patientInfo = new Patient();
        patientInfo.setId(PATIENT_ID);
        patientInfo.setName("Paciente Ana Costa");
        patientInfo.setCpf("12345678904");
        patientInfo.setEmail("ana.costa@medsync.com");
        patientInfo.setDateOfBirth(LocalDate.parse("1990-12-10"));
        return patientInfo;
    }

    public static Doctor getDoctor() {
        Doctor doctorInfo = new Doctor();
        doctorInfo.setId("850e8400-e29b-41d4-a716-446655440002");
        doctorInfo.setName("Dr. João Silva");
        doctorInfo.setSpecialty("Cardiologia");
        return doctorInfo;
    }

    public static CreateUser getCreateUser() {
        CreateUser userInfo = new CreateUser();
        userInfo.setId(USER_ID);
        userInfo.setName("Admin Sistema");
        userInfo.setRole("ADMIN");
        return userInfo;
    }

    public static ActionLog getActionLog(CreateUser userInfo) {
        ActionLog actionLog = new ActionLog();
        actionLog.setActionType(ActionType.CREATION);
        actionLog.setTimestamp(LocalDateTime.parse("2025-10-05T11:53:43"));
        actionLog.setUser(userInfo);
        return actionLog;
    }

    public static AppointmentInput getAppointmentInput(String eventType) {
        return new AppointmentInput(
                UUID.randomUUID().toString(),
                LocalDateTime.now().plusDays(5),
                "SCHEDULED",
                "Consulta de rotina",
                eventType,
                LocalDateTime.now(),
                PATIENT_ID,
                "João da Silva",
                "11122233344",
                "joao.silva@email.com",
                LocalDate.of(1980, 1, 15),
                UUID.randomUUID().toString(),
                "Dra. Ana Costa",
                "55566677788",
                "ana.costa@hospital.com",
                "Cardiologia",
                UUID.randomUUID().toString(),
                "Enfermeira Beatriz",
                "beatriz.enf@hospital.com",
                "NURSE"
        );
    }


}
