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

import static com.medsync.historico.application.TestUtils.APPOINTMENT_ID;
import static com.medsync.historico.application.TestUtils.USER_ID;
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
            Appointment expected = mock(Appointment.class);

            when(medicalHistoryGateway.findAppointmentById(APPOINTMENT_ID, USER_ID)).thenReturn(expected);

            Appointment result = getAppointmentByIdUseCase.execute(APPOINTMENT_ID, USER_ID);

            assertEquals(expected, result);
            verify(medicalHistoryGateway).findAppointmentById(APPOINTMENT_ID, USER_ID);
        }

        @Test
        @DisplayName("Should throw MedicalHistoryNotFoundException when gateway throws NoSuchElementException")
        void shouldThrowMedicalHistoryNotFoundExceptionWhenGatewayThrowsNoSuchElementException() {

            when(medicalHistoryGateway.findAppointmentById(APPOINTMENT_ID, USER_ID)).thenThrow(new NoSuchElementException());

            assertThrows(MedicalHistoryNotFoundException.class, () -> getAppointmentByIdUseCase.execute(APPOINTMENT_ID, USER_ID));
        }

    }

}