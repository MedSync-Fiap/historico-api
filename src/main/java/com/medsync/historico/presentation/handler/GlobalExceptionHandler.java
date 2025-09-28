package com.medsync.historico.presentation.handler;

import com.medsync.historico.application.exceptions.AppointmentNotFoundException;
import com.medsync.historico.application.exceptions.MedicalHistoryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MedicalHistoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMedicalHistoryNotFoundException(MedicalHistoryNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("MEDICAL_HISTORY_NOT_FOUND", ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAppointmentNotFoundException(AppointmentNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("APPOINTMENT_NOT_FOUND", ex.getMessage(), LocalDateTime.now()));
    }

    public record ErrorResponse(
            String code,
            String message,
            LocalDateTime timestamp
    ) {}

}
