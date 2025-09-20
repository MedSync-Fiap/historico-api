package com.medsync.historico.application.exceptions;

public class MedicalHistoryNotFoundException extends RuntimeException {

    public MedicalHistoryNotFoundException(String id) {
        super("Medical history not found for patient with ID: " + id);
    }

}
