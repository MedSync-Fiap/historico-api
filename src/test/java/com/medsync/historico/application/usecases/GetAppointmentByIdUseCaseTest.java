package com.medsync.historico.application.usecases;

import com.medsync.historico.application.exceptions.MedicalHistoryNotFoundException;
import com.medsync.historico.domain.entities.Appointment;
import com.medsync.historico.domain.gateways.MedicalHistoryGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAppointmentByIdUseCaseTest {

    @Mock
    private MedicalHistoryGateway medicalHistoryGateway;

    @InjectMocks
    private GetAppointmentByIdUseCase getAppointmentByIdUseCase;

    @Nested
    class Execute {

        @Test
        @DisplayName("Should return appointment when found by gateway")
        void shouldReturnAppointmentWhenFoundByGateway() {
            Long appointmentId = 1L;
            Long patientId = 2L;
            Appointment expected = mock(Appointment.class);

            when(medicalHistoryGateway.findAppointmentById(appointmentId, patientId)).thenReturn(expected);

            Appointment result = getAppointmentByIdUseCase.execute(appointmentId, patientId);

            assertEquals(expected, result);
            verify(medicalHistoryGateway).findAppointmentById(appointmentId, patientId);
        }

        @Test
        @DisplayName("Should throw MedicalHistoryNotFoundException when gateway throws NoSuchElementException")
        void shouldThrowMedicalHistoryNotFoundExceptionWhenGatewayThrowsNoSuchElementException() {
            Long appointmentId = 1L;
            Long patientId = 2L;

            when(medicalHistoryGateway.findAppointmentById(appointmentId, patientId)).thenThrow(new NoSuchElementException());

            assertThrows(MedicalHistoryNotFoundException.class, () -> getAppointmentByIdUseCase.execute(appointmentId, patientId));
        }

    }

}