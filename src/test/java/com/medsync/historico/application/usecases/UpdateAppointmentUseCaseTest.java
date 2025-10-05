package com.medsync.historico.application.usecases;

import com.medsync.historico.application.dto.AppointmentInput;
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

import static com.medsync.historico.application.TestUtils.APPOINTMENT_ID;
import static com.medsync.historico.application.TestUtils.USER_ID;
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
        @DisplayName("Should update appointment fields and save medical history when input is valid")
        void shouldUpdateAppointmentFieldsAndSaveMedicalHistoryWhenEventIsValid() {
            AppointmentInput input = mock(AppointmentInput.class);
            when(input.consultaId()).thenReturn(APPOINTMENT_ID);
            when(input.medicoId()).thenReturn(USER_ID);

            Doctor doctor = mock(Doctor.class);
            when(doctor.getId()).thenReturn(USER_ID);

            Appointment appointment = mock(Appointment.class);
            when(appointment.getId()).thenReturn(APPOINTMENT_ID);
            when(appointment.getDoctor()).thenReturn(doctor);
            when(appointment.getActionLogs()).thenReturn(new java.util.ArrayList<>());

            MedicalHistory medicalHistory = mock(MedicalHistory.class);
            when(medicalHistory.getAppointments()).thenReturn(List.of(appointment));
            when(medicalHistoryGateway.save(medicalHistory)).thenReturn(medicalHistory);

            MedicalHistory result = useCase.execute(input, medicalHistory);

            verify(appointment).updateFieldsWithNewValues(input);
            verify(appointment, never()).updateDoctor(input);
            assertEquals(medicalHistory, result);
            assertEquals(1, appointment.getActionLogs().size());
            assertNotNull(appointment.getActionLogs().getFirst());
            assertEquals(ActionType.EDITION, appointment.getActionLogs().getFirst().getActionType());
        }

        @Test
        @DisplayName("Should update doctor when doctor is different")
        void shouldUpdateDoctorWhenDoctorIsDifferent() {
            AppointmentInput input = mock(AppointmentInput.class);
            when(input.consultaId()).thenReturn(APPOINTMENT_ID);
            when(input.medicoId()).thenReturn("different-user-id");

            Doctor doctor = mock(Doctor.class);
            when(doctor.getId()).thenReturn(USER_ID);

            Appointment appointment = mock(Appointment.class);
            when(appointment.getId()).thenReturn(APPOINTMENT_ID);
            when(appointment.getDoctor()).thenReturn(doctor);
            when(appointment.getActionLogs()).thenReturn(new java.util.ArrayList<>());

            MedicalHistory medicalHistory = mock(MedicalHistory.class);
            when(medicalHistory.getAppointments()).thenReturn(List.of(appointment));
            when(medicalHistoryGateway.save(medicalHistory)).thenReturn(medicalHistory);

            useCase.execute(input, medicalHistory);

            verify(appointment).updateDoctor(input);
        }

        @Test
        @DisplayName("Should throw AppointmentNotFoundException when appointment not found")
        void shouldThrowExceptionWhenAppointmentNotFound() {
            AppointmentInput input = mock(AppointmentInput.class);
            when(input.consultaId()).thenReturn("non-existent-id");

            MedicalHistory medicalHistory = mock(MedicalHistory.class);
            when(medicalHistory.getAppointments()).thenReturn(Collections.emptyList());

            assertThrows(AppointmentNotFoundException.class, () -> useCase.execute(input, medicalHistory));
        }

    }

}