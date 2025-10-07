package com.medsync.historico.application.services;

import com.medsync.historico.application.dto.AppointmentInput;
import com.medsync.historico.application.exceptions.MedicalHistoryNotFoundException;
import com.medsync.historico.application.usecases.*;
import com.medsync.historico.domain.entities.Appointment;
import com.medsync.historico.domain.entities.MedicalHistory;
import com.medsync.historico.domain.gateways.MedicalHistoryGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.medsync.historico.TestUtils.USER_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalHistoryServiceTest {

    @Mock
    private SaveNewAppointmentUseCase saveNewAppointmentUseCase;

    @Mock
    private CreateMedicalHistoryUseCase createMedicalHistoryUseCase;

    @Mock
    private UpdateAppointmentUseCase updateAppointmentUseCase;

    @Mock
    private GetMedicalHistoryByPatientIdUseCase getMedicalHistoryByPatientIdUseCase;

    @Mock
    private MedicalHistoryGateway medicalHistoryGateway;

    @Mock
    private GetAppointmentByIdUseCase getAppointmentByIdUseCase;

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

    @Nested
    class UpdateAppointmentInMedicalHistory {

        @Test
        @DisplayName("Should update appointment in existing medical history")
        void shouldUpdateAppointmentInExistingMedicalHistory() {
            AppointmentInput input = mock(AppointmentInput.class);
            MedicalHistory existing = mock(MedicalHistory.class);
            MedicalHistory updated = mock(MedicalHistory.class);

            when(input.pacienteId()).thenReturn(USER_ID);
            doReturn(existing)
                    .when(medicalHistoryService).getMedicalHistoryByPatientId(USER_ID);
            when(updateAppointmentUseCase.execute(input, existing)).thenReturn(updated);

            MedicalHistory result = medicalHistoryService.updateAppointmentInMedicalHistory(input);

            assertEquals(updated, result);
            verify(updateAppointmentUseCase).execute(input, existing);
        }

    }

    @Nested
    class GetMedicalHistoryByPatientId {

        @Test
        @DisplayName("Should return medical history for valid patient id")
        void shouldReturnMedicalHistoryForValidPatientId() {
            MedicalHistory expected = mock(MedicalHistory.class);

            when(getMedicalHistoryByPatientIdUseCase.execute(USER_ID)).thenReturn(expected);

            MedicalHistory result = medicalHistoryService.getMedicalHistoryByPatientId(USER_ID);

            assertEquals(expected, result);
            verify(getMedicalHistoryByPatientIdUseCase).execute(USER_ID);
        }

        @Test
        @DisplayName("Should throw MedicalHistoryNotFoundException for invalid patient id")
        void shouldThrowMedicalHistoryNotFoundExceptionForInvalidPatientId() {
            when(getMedicalHistoryByPatientIdUseCase.execute(USER_ID))
                    .thenThrow(new MedicalHistoryNotFoundException(USER_ID));

            assertThrows(MedicalHistoryNotFoundException.class, () -> medicalHistoryService.getMedicalHistoryByPatientId(USER_ID));
        }

    }

    @Nested
    class GetAppointmentById {

        @Test
        @DisplayName("Should return appointment for valid appointment and patient id")
        void shouldReturnAppointmentForValidAppointmentAndPatientId() {
            String appointmentId = "appointment-1";
            Appointment expected = mock(Appointment.class);

            when(getAppointmentByIdUseCase.execute(appointmentId, USER_ID)).thenReturn(expected);

            Appointment result = medicalHistoryService.getAppointmentById(appointmentId, USER_ID);

            assertEquals(expected, result);
            verify(getAppointmentByIdUseCase).execute(appointmentId, USER_ID);
        }

    }

    @Nested
    class GetAllMedicalHistories {

        @Test
        @DisplayName("Should return all medical histories")
        void shouldReturnAllMedicalHistories() {
            List<MedicalHistory> expected = List.of(mock(MedicalHistory.class), mock(MedicalHistory.class));

            when(medicalHistoryGateway.findAll()).thenReturn(expected);

            List<MedicalHistory> result = medicalHistoryService.getAllMedicalHistories();

            assertEquals(expected, result);
            verify(medicalHistoryGateway).findAll();
        }

    }

}