package com.medsync.historico.application.usecases;

import com.medsync.historico.application.dto.AppointmentEvent;
import com.medsync.historico.domain.entities.Appointment;
import com.medsync.historico.domain.entities.MedicalHistory;
import com.medsync.historico.domain.entities.Patient;
import com.medsync.historico.domain.gateways.MedicalHistoryGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateMedicalHistoryUseCaseTest {

    @Mock
    private MedicalHistoryGateway medicalHistoryGateway;

    @InjectMocks
    private CreateMedicalHistoryUseCase createMedicalHistoryUseCase;

    @Nested
    class Execute {

        @Test
        @DisplayName("Should create and save medical history with valid patient and appointment")
        void shouldCreateAndSaveMedicalHistoryWithValidPatientAndAppointment() {
            AppointmentEvent event = mock(AppointmentEvent.class);
            MedicalHistory expected = mock(MedicalHistory.class);

            when(medicalHistoryGateway.save(any(MedicalHistory.class))).thenReturn(expected);

            MedicalHistory result = createMedicalHistoryUseCase.execute(event);

            assertEquals(expected, result);
            verify(medicalHistoryGateway).save(any(MedicalHistory.class));
        }

        @Test
        @DisplayName("Should create medical history with a single appointment")
        void shouldCreateMedicalHistoryWithSingleAppointment() {
            AppointmentEvent event = mock(AppointmentEvent.class);

            when(medicalHistoryGateway.save(any(MedicalHistory.class))).thenAnswer(invocation -> invocation.getArgument(0));

            MedicalHistory result = createMedicalHistoryUseCase.execute(event);

            List<Appointment> appointments = result.getAppointments();
            assertNotNull(appointments);
            assertEquals(1, appointments.size());
        }

        @Test
        @DisplayName("Should create medical history with non-null patient")
        void shouldCreateMedicalHistoryWithNonNullPatient() {
            AppointmentEvent event = mock(AppointmentEvent.class);

            when(medicalHistoryGateway.save(any(MedicalHistory.class))).thenAnswer(invocation -> invocation.getArgument(0));

            MedicalHistory result = createMedicalHistoryUseCase.execute(event);

            Patient patient = result.getPatient();
            assertNotNull(patient);
        }

    }

}