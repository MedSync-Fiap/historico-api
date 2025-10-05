package com.medsync.historico.application.services;

import com.medsync.historico.application.dto.AppointmentInput;
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

import static com.medsync.historico.application.TestUtils.USER_ID;
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
            AppointmentInput input = mock(AppointmentInput.class);
            MedicalHistory expected = mock(MedicalHistory.class);

            when(createMedicalHistoryUseCase.execute(input)).thenReturn(expected);

            MedicalHistory result = medicalHistoryService.createMedicalHistory(input);

            assertEquals(expected, result);
            verify(createMedicalHistoryUseCase).execute(input);
        }

    }

    @Nested
    class AddAppointmentInMedicalHistory {

        @Test
        @DisplayName("Should add appointment to existing medical history when found")
        void shouldAddAppointmentToExistingMedicalHistoryWhenFound() {
            AppointmentInput input = mock(AppointmentInput.class);
            MedicalHistory existing = mock(MedicalHistory.class);
            MedicalHistory updated = mock(MedicalHistory.class);

            when(input.pacienteId()).thenReturn(USER_ID);
            doReturn(existing)
                .when(medicalHistoryService).getMedicalHistoryByPatientId(USER_ID);
            when(saveNewAppointmentUseCase.execute(input, existing)).thenReturn(updated);

            MedicalHistory result = medicalHistoryService.addAppointmentInMedicalHistory(input);

            assertEquals(updated, result);
            verify(saveNewAppointmentUseCase).execute(input, existing);
        }

        @Test
        @DisplayName("Should create new medical history if not found when adding appointment")
        void shouldCreateNewMedicalHistoryIfNotFoundWhenAddingAppointment() {
            AppointmentInput input = mock(AppointmentInput.class);
            MedicalHistory created = mock(MedicalHistory.class);

            when(input.pacienteId()).thenReturn(USER_ID);
            doThrow(new MedicalHistoryNotFoundException(USER_ID))
                .when(medicalHistoryService).getMedicalHistoryByPatientId(USER_ID);
            when(createMedicalHistoryUseCase.execute(input)).thenReturn(created);

            MedicalHistory result = medicalHistoryService.addAppointmentInMedicalHistory(input);

            assertEquals(created, result);
            verify(createMedicalHistoryUseCase).execute(input);
        }

    }


}