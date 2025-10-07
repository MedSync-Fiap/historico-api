package com.medsync.historico.application.usecases;

import com.medsync.historico.application.exceptions.MedicalHistoryNotFoundException;
import com.medsync.historico.domain.entities.MedicalHistory;
import com.medsync.historico.domain.gateways.MedicalHistoryGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.medsync.historico.TestUtils.PATIENT_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetMedicalHistoryByPatientIdUseCaseTest {
    
    @Mock
    private MedicalHistoryGateway medicalHistoryGateway;

    @Nested
    class Execute {

        @Test
        @DisplayName("Should return medical history when it exists for the patient")
        void shouldReturnMedicalHistoryWhenItExistsForPatient() {
            MedicalHistory expected = mock(MedicalHistory.class);

            when(medicalHistoryGateway.findByPatientId(PATIENT_ID)).thenReturn(Optional.of(expected));

            GetMedicalHistoryByPatientIdUseCase useCase = new GetMedicalHistoryByPatientIdUseCase(medicalHistoryGateway);
            MedicalHistory result = useCase.execute(PATIENT_ID);

            assertEquals(expected, result);
            verify(medicalHistoryGateway).findByPatientId(PATIENT_ID);
        }

        @Test
        @DisplayName("Should throw exception when no medical history exists for the patient")
        void shouldThrowExceptionWhenNoMedicalHistoryExistsForPatient() {
            when(medicalHistoryGateway.findByPatientId(PATIENT_ID)).thenReturn(Optional.empty());

            GetMedicalHistoryByPatientIdUseCase useCase = new GetMedicalHistoryByPatientIdUseCase(medicalHistoryGateway);

            MedicalHistoryNotFoundException exception = assertThrows(MedicalHistoryNotFoundException.class, () -> useCase.execute(PATIENT_ID));

            assertEquals("Medical history not found for patient with ID: " + PATIENT_ID, exception.getMessage());
            verify(medicalHistoryGateway).findByPatientId(PATIENT_ID);
        }
        
    }

}