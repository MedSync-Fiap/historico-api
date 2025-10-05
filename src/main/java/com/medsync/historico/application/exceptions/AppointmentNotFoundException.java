package com.medsync.historico.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class AppointmentNotFoundException extends RuntimeException {

    public AppointmentNotFoundException(String id) {
        super("Appointment with ID " + id + " not found.");
    }

}
