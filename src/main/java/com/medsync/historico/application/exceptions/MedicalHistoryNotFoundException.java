package com.medsync.historico.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class MedicalHistoryNotFoundException extends RuntimeException {

    public MedicalHistoryNotFoundException(Long id) {
        super("Medical history not found for patient with ID: " + id);
    }

}
