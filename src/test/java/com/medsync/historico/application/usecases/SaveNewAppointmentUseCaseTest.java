package com.medsync.historico.application.usecases;

import com.medsync.historico.application.dto.AppointmentEvent;
import com.medsync.historico.domain.entities.Appointment;
import com.medsync.historico.domain.entities.MedicalHistory;
import com.medsync.historico.domain.gateways.MedicalHistoryGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveNewAppointmentUseCaseTest {

    @Mock
    private MedicalHistoryGateway medicalHistoryGateway;

    @InjectMocks
    private SaveNewAppointmentUseCase saveNewAppointmentUseCase;

    @Nested
    class Execute {

        @Test
        @DisplayName("Should add new appointment to existing list and save medical history")
        void shouldAddNewAppointmentToExistingListAndSave() {
            AppointmentEvent event = mock(AppointmentEvent.class);
            MedicalHistory medicalHistory = mock(MedicalHistory.class);
            List<Appointment> existingAppointments = new ArrayList<>();
            when(medicalHistory.getAppointments()).thenReturn(existingAppointments);
            when(medicalHistoryGateway.save(medicalHistory)).thenReturn(medicalHistory);

            MedicalHistory result = saveNewAppointmentUseCase.execute(event, medicalHistory);

            assertEquals(medicalHistory, result);
            assertEquals(1, existingAppointments.size());
        }

        @Test
        @DisplayName("Should create new appointment list if null and save medical history")
        void shouldCreateNewAppointmentListIfNullAndSave() {
            AppointmentEvent event = mock(AppointmentEvent.class);
            MedicalHistory medicalHistory = mock(MedicalHistory.class);
            when(medicalHistory.getAppointments()).thenReturn(null);
            when(medicalHistoryGateway.save(medicalHistory)).thenReturn(medicalHistory);

            MedicalHistory result = saveNewAppointmentUseCase.execute(event, medicalHistory);

            verify(medicalHistory).setAppointments(anyList());
            assertEquals(medicalHistory, result);
        }

        @Test
        @DisplayName("Should return null if gateway returns null")
        void shouldReturnNullIfGatewayReturnsNull() {
            AppointmentEvent event = mock(AppointmentEvent.class);
            MedicalHistory medicalHistory = mock(MedicalHistory.class);
            when(medicalHistory.getAppointments()).thenReturn(new java.util.ArrayList<>());
            when(medicalHistoryGateway.save(medicalHistory)).thenReturn(null);

            assertNull(saveNewAppointmentUseCase.execute(event, medicalHistory));
        }

    }

}