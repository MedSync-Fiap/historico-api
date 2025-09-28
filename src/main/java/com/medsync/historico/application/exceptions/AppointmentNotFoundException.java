package com.medsync.historico.application.exceptions;

public class AppointmentNotFoundException extends RuntimeException {

    public AppointmentNotFoundException(Long id) {
        super("Appointment with ID " + id + " not found.");
    }

}
