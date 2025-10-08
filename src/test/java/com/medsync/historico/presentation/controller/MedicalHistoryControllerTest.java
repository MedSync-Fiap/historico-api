package com.medsync.historico.presentation.controller;

import com.medsync.historico.application.dto.AppointmentInput;
import com.medsync.historico.application.services.MedicalHistoryService;
import com.medsync.historico.domain.entities.Appointment;
import com.medsync.historico.domain.entities.MedicalHistory;
import com.medsync.historico.domain.entities.Patient;
import com.medsync.historico.domain.enums.EventType;
import com.medsync.historico.presentation.dto.AppointmentFilterInput;
import com.medsync.historico.presentation.dto.AppointmentResponse;
import com.medsync.historico.presentation.dto.MedicalHistoryResponse;
import com.medsync.historico.presentation.mappers.AppointmentDTOMapper;
import com.medsync.historico.presentation.mappers.MedicalHistoryDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Objects;

import static com.medsync.historico.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@GraphQlTest(MedicalHistoryController.class)
class MedicalHistoryControllerTest {

    @MockitoBean
    private MedicalHistoryService medicalHistoryService;

    @MockitoBean
    private MedicalHistoryDTOMapper medicalHistoryMapper;

    @MockitoBean
    private AppointmentDTOMapper appointmentMapper;

    @Autowired
    private GraphQlTester graphQlTester;

    @InjectMocks
    private MedicalHistoryController controller;

    private MedicalHistory mockMedicalHistory;

    private AppointmentResponse mockAppointmentResponse;


    @BeforeEach
    void setUp() {
        mockMedicalHistory = new MedicalHistory();
        mockMedicalHistory.setId("hist-123");

        Patient patientInfo = getPatient();
        mockMedicalHistory.setPatient(patientInfo);
        Appointment futureAppointment = getAppointment(false);
        Appointment pastAppointment = getAppointment(true);
        mockMedicalHistory.setAppointments(List.of(futureAppointment, pastAppointment));

        MedicalHistoryResponse mockMedicalHistoryResponse = new MedicalHistoryResponse();
        mockMedicalHistoryResponse.setPatient(patientInfo);
        mockMedicalHistoryResponse.setAppointments(mockMedicalHistory.getAppointments());

        mockAppointmentResponse = new AppointmentResponse(
                futureAppointment.getId(),
                futureAppointment.getDoctor(),
                futureAppointment.getCreateUser(),
                futureAppointment.getAppointmentDateTime(),
                futureAppointment.getStatus(),
                futureAppointment.getClinicalNotes()
        );

        when(medicalHistoryMapper.toResponse(any(MedicalHistory.class)))
                .thenReturn(mockMedicalHistoryResponse);
        when(appointmentMapper.toResponse(any(Appointment.class)))
                .thenReturn(mockAppointmentResponse);
    }

    @Test
    @DisplayName("Should return medical history by patient ID")
    void getMedicalHistoryByPatientId_shouldReturnHistory() {
        when(medicalHistoryService.getMedicalHistoryByPatientId(PATIENT_ID)).thenReturn(mockMedicalHistory);

        graphQlTester
                .documentName("getHistoryByPatient")
                .variable("patientId", PATIENT_ID)
                .execute()
                .path("data.getMedicalHistoryByPatientId")
                .hasValue()
                .entity(MedicalHistoryResponse.class)
                .satisfies(history -> {
                    assertEquals(PATIENT_ID, history.getPatient().getId());
                    assertEquals(mockMedicalHistory.getAppointments().getFirst().getId(), history.getAppointments().getFirst().getId());
                });
    }

    @Test
    @DisplayName("Should return history with only future appointments when filtered")
    void getMedicalHistoryByPatientId_shouldReturnHistory_withFilter() {
        when(medicalHistoryService.getMedicalHistoryByPatientId(PATIENT_ID)).thenReturn(mockMedicalHistory);

        graphQlTester
                .documentName("getHistoryByPatientWithFutureAppointments")
                .variable("patientId", PATIENT_ID)
                .execute()
                .path("data.getMedicalHistoryByPatientId")
                .hasValue()
                .entity(MedicalHistoryResponse.class)
                .satisfies(history -> {
                    assertEquals(PATIENT_ID, history.getPatient().getId());
                    assertEquals(1, history.getAppointments().size());
                    assertEquals(mockMedicalHistory.getAppointments().getFirst().getId(), history.getAppointments().getFirst().getId());
                });
    }

    @Test
    @DisplayName("Should return history with all appointments when filter is false")
    void getMedicalHistoryByPatientId_shouldReturnHistory_withAllAppointments_whenOnlyFutureFalse() {
        when(medicalHistoryService.getMedicalHistoryByPatientId(PATIENT_ID)).thenReturn(mockMedicalHistory);

        graphQlTester
                .documentName("getHistoryByPatientWithAllAppointments")
                .variable("patientId", PATIENT_ID)
                .execute()
                .path("data.getMedicalHistoryByPatientId")
                .hasValue()
                .entity(MedicalHistoryResponse.class)
                .satisfies(history -> {
                    assertEquals(PATIENT_ID, history.getPatient().getId());
                    assertEquals(2, history.getAppointments().size());
                    assertEquals(mockMedicalHistory.getAppointments().getFirst().getId(), history.getAppointments().getFirst().getId());
                    assertEquals(mockMedicalHistory.getAppointments().getLast().getId(), history.getAppointments().getLast().getId());
                });
    }

    @Test
    @DisplayName("Should return a specific appointment successfully")
    void getAppointmentById_Success() {
        when(medicalHistoryService.getAppointmentById(APPOINTMENT_ID, PATIENT_ID)).thenReturn(getAppointment(false));

        graphQlTester.documentName("getAppointmentById")
                .variable("appointmentId", APPOINTMENT_ID)
                .variable("patientId", PATIENT_ID)
                .execute()
                .path("getAppointmentById")
                .entity(AppointmentResponse.class)
                .satisfies(response -> {
                    assertEquals(mockAppointmentResponse.getId(), response.getId());
                    assertEquals(mockAppointmentResponse.getDoctor().getName(), response.getDoctor().getName());
                });
    }

    @Test
    @DisplayName("Should save new appointment and return updated history")
    void saveNewAppointment_shouldReturnUpdatedHistory() {
        AppointmentInput input = getAppointmentInput(EventType.CREATION.getValue());

        when(medicalHistoryService.addAppointmentInMedicalHistory(any(AppointmentInput.class)))
                .thenReturn(mockMedicalHistory);

        graphQlTester
                .documentName("saveNewAppointment")
                .variable("input", convertAppointmentInputToMap(input))
                .execute()
                .path("data.saveNewAppointment")
                .hasValue()
                .entity(MedicalHistoryResponse.class)
                .satisfies(history -> {
                    assertEquals(PATIENT_ID, history.getPatient().getId());
                    assertEquals(mockMedicalHistory.getAppointments().getFirst().getId(), history.getAppointments().getFirst().getId());
                });

    }

    @Test
    @DisplayName("Should fail to save appointment with invalid event type")
    void saveNewAppointment_InvalidEventType_ShouldFail() {
        AppointmentInput input = getAppointmentInput("INVALID_EVENT");

        graphQlTester.documentName("saveNewAppointment")
                .variable("input", convertAppointmentInputToMap(input))
                .execute()
                .errors()
                .expect(error -> Objects.requireNonNull(error.getMessage()).contains("Unsupported event type for creation: INVALID_EVENT"))
                .verify();
    }

    @Test
    @DisplayName("Should update an existing appointment successfully")
    void updateAppointment_Success() {
        AppointmentInput input = getAppointmentInput(EventType.EDITION.getValue());

        when(medicalHistoryService.updateAppointmentInMedicalHistory(any(AppointmentInput.class))).thenReturn(mockMedicalHistory);

        graphQlTester.documentName("updateAppointment")
                .variable("input", convertAppointmentInputToMap(input))
                .execute()
                .path("updateAppointment")
                .entity(MedicalHistoryResponse.class)
                .satisfies(response -> {
                    assertEquals(PATIENT_ID, response.getPatient().getId());
                    assertEquals(APPOINTMENT_ID, response.getAppointments().getFirst().getId());
                });
    }

    @Test
    @DisplayName("Should fail to update appointment with invalid event type")
    void updateAppointment_InvalidEventType_ShouldFail() {
        AppointmentInput input = getAppointmentInput("INVALID_EVENT");

        graphQlTester.documentName("updateAppointment")
                .variable("input", convertAppointmentInputToMap(input))
                .execute()
                .errors()
                .expect(error -> Objects.requireNonNull(error.getMessage()).contains("Unsupported event type for update: INVALID_EVENT"))
                .verify();
    }

    @Nested
    @DisplayName("Get Appointments with Filtering")
    class GetAppointmentsWithFiltering {

        @Test
        @DisplayName("Should return all appointments when filter is null")
        void getAppointments_FilterNull_ReturnsAllAppointments() {
            MedicalHistoryResponse history = new MedicalHistoryResponse();

            history.setAppointments(List.of(getAppointment(true), getAppointment(false)));

            List<Appointment> result = controller.getAppointments(history, null);

            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("Should return all appointments when onlyFuture is null")
        void getAppointments_OnlyFutureNull_ReturnsAllAppointments() {
            MedicalHistoryResponse history = new MedicalHistoryResponse();
            history.setAppointments(List.of(getAppointment(true), getAppointment(false)));

            AppointmentFilterInput filter = new AppointmentFilterInput(null);

            List<Appointment> result = controller.getAppointments(history, filter);

            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("Should return all appointments when onlyFuture is false")
        void getAppointments_OnlyFutureFalse_ReturnsAllAppointments() {
            MedicalHistoryResponse history = new MedicalHistoryResponse();
            history.setAppointments(List.of(getAppointment(true), getAppointment(false)));

            AppointmentFilterInput filter = new AppointmentFilterInput(false);

            List<Appointment> result = controller.getAppointments(history, filter);

            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("Should return only future appointments when onlyFuture is true")
        void getAppointments_OnlyFutureTrue_ReturnsOnlyFutureAppointments() {
            MedicalHistoryResponse history = new MedicalHistoryResponse();

            Appointment futureAppointment = getAppointment(false);

            history.setAppointments(List.of(getAppointment(true), futureAppointment));

            AppointmentFilterInput filter = new AppointmentFilterInput(true);

            List<Appointment> result = controller.getAppointments(history, filter);

            assertEquals(1, result.size());
            assertEquals(futureAppointment.getId(), result.getFirst().getId());
        }

        @Test
        @DisplayName("Should return empty list when no appointments are in the future")
        void getAppointments_OnlyFutureTrue_NoFutureAppointments_ReturnsEmptyList() {
            MedicalHistoryResponse history = new MedicalHistoryResponse();
            history.setAppointments(List.of(getAppointment(true)));

            AppointmentFilterInput filter = new AppointmentFilterInput(true);

            List<Appointment> result = controller.getAppointments(history, filter);

            assertTrue(result.isEmpty());
        }

    }

}