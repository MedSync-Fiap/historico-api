package com.medsync.historico.application.usecases;

import com.medsync.historico.application.dto.AppointmentEvent;
import com.medsync.historico.application.exceptions.AppointmentNotFoundException;
import com.medsync.historico.domain.entities.Appointment;
import com.medsync.historico.domain.entities.Doctor;
import com.medsync.historico.domain.entities.MedicalHistory;
import com.medsync.historico.domain.enums.ActionType;
import com.medsync.historico.domain.gateways.MedicalHistoryGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateAppointmentUseCaseTest {

    @Mock
    private MedicalHistoryGateway medicalHistoryGateway;

    @InjectMocks
    private UpdateAppointmentUseCase useCase;

    @Nested
    class Execute {

        @Test
        @DisplayName("Should update appointment fields and save medical history when event is valid")
        void shouldUpdateAppointmentFieldsAndSaveMedicalHistoryWhenEventIsValid() {
            AppointmentEvent event = mock(AppointmentEvent.class);
            when(event.consultaId()).thenReturn(1L);
            when(event.medicoId()).thenReturn(2L);

            Doctor doctor = mock(Doctor.class);
            when(doctor.getId()).thenReturn(2L);

            Appointment appointment = mock(Appointment.class);
            when(appointment.getId()).thenReturn(1L);
            when(appointment.getDoctor()).thenReturn(doctor);
            when(appointment.getActionLogs()).thenReturn(new java.util.ArrayList<>());

            MedicalHistory medicalHistory = mock(MedicalHistory.class);
            when(medicalHistory.getAppointments()).thenReturn(List.of(appointment));
            when(medicalHistoryGateway.save(medicalHistory)).thenReturn(medicalHistory);

            MedicalHistory result = useCase.execute(event, medicalHistory);

            verify(appointment).updateFieldsWithNewValues(event);
            verify(appointment, never()).updateDoctor(event);
            assertEquals(medicalHistory, result);
            assertEquals(1, appointment.getActionLogs().size());
            assertNotNull(appointment.getActionLogs().getFirst());
            assertEquals(ActionType.EDITION, appointment.getActionLogs().getFirst().getActionType());
        }

        @Test
        @DisplayName("Should update doctor when doctor is different")
        void shouldUpdateDoctorWhenDoctorIsDifferent() {
            AppointmentEvent event = mock(AppointmentEvent.class);
            when(event.consultaId()).thenReturn(1L);
            when(event.medicoId()).thenReturn(3L);

            Doctor doctor = mock(Doctor.class);
            when(doctor.getId()).thenReturn(2L);

            Appointment appointment = mock(Appointment.class);
            when(appointment.getId()).thenReturn(1L);
            when(appointment.getDoctor()).thenReturn(doctor);
            when(appointment.getActionLogs()).thenReturn(new java.util.ArrayList<>());

            MedicalHistory medicalHistory = mock(MedicalHistory.class);
            when(medicalHistory.getAppointments()).thenReturn(List.of(appointment));
            when(medicalHistoryGateway.save(medicalHistory)).thenReturn(medicalHistory);

            useCase.execute(event, medicalHistory);

            verify(appointment).updateDoctor(event);
        }

        @Test
        @DisplayName("Should throw AppointmentNotFoundException when appointment not found")
        void shouldThrowExceptionWhenAppointmentNotFound() {
            AppointmentEvent event = mock(AppointmentEvent.class);
            when(event.consultaId()).thenReturn(99L);

            MedicalHistory medicalHistory = mock(MedicalHistory.class);
            when(medicalHistory.getAppointments()).thenReturn(Collections.emptyList());

            assertThrows(AppointmentNotFoundException.class, () -> useCase.execute(event, medicalHistory));
        }

    }

}