package com.medsync.historico.application.services;

import com.medsync.historico.application.dto.AppointmentEvent;
import com.medsync.historico.application.exceptions.MedicalHistoryNotFoundException;
import com.medsync.historico.application.usecases.CreateMedicalHistoryUseCase;
import com.medsync.historico.application.usecases.SaveNewAppointmentUseCase;
import com.medsync.historico.domain.entities.MedicalHistory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalHistoryServiceTest {

    @Mock
    private SaveNewAppointmentUseCase saveNewAppointmentUseCase;

    @Mock
    private CreateMedicalHistoryUseCase createMedicalHistoryUseCase;

    @Spy
    @InjectMocks
    private MedicalHistoryService medicalHistoryService;

    @Nested
    class CreateMedicalHistory {

        @Test
        @DisplayName("Should delegate creation to use case and return created medical history")
        void shouldDelegateCreationToUseCaseAndReturnCreatedMedicalHistory() {
            AppointmentEvent event = mock(AppointmentEvent.class);
            MedicalHistory expected = mock(MedicalHistory.class);

            when(createMedicalHistoryUseCase.execute(event)).thenReturn(expected);

            MedicalHistory result = medicalHistoryService.createMedicalHistory(event);

            assertEquals(expected, result);
            verify(createMedicalHistoryUseCase).execute(event);
        }

    }

    @Nested
    class AddAppointmentInMedicalHistory {

        @Test
        @DisplayName("Should add appointment to existing medical history when found")
        void shouldAddAppointmentToExistingMedicalHistoryWhenFound() {
            AppointmentEvent event = mock(AppointmentEvent.class);
            MedicalHistory existing = mock(MedicalHistory.class);
            MedicalHistory updated = mock(MedicalHistory.class);

            when(event.pacienteId()).thenReturn(1L);
            doReturn(existing)
                .when(medicalHistoryService).getMedicalHistoryByPatientId(1L);
            when(saveNewAppointmentUseCase.execute(event, existing)).thenReturn(updated);

            MedicalHistory result = medicalHistoryService.addAppointmentInMedicalHistory(event);

            assertEquals(updated, result);
            verify(saveNewAppointmentUseCase).execute(event, existing);
        }

        @Test
        @DisplayName("Should create new medical history if not found when adding appointment")
        void shouldCreateNewMedicalHistoryIfNotFoundWhenAddingAppointment() {
            AppointmentEvent event = mock(AppointmentEvent.class);
            MedicalHistory created = mock(MedicalHistory.class);

            when(event.pacienteId()).thenReturn(2L);
            doThrow(new MedicalHistoryNotFoundException(2L))
                .when(medicalHistoryService).getMedicalHistoryByPatientId(2L);
            when(createMedicalHistoryUseCase.execute(event)).thenReturn(created);

            MedicalHistory result = medicalHistoryService.addAppointmentInMedicalHistory(event);

            assertEquals(created, result);
            verify(createMedicalHistoryUseCase).execute(event);
        }

    }


}