package com.medsync.historico.presentation.controller;

import com.medsync.historico.application.services.MedicalHistoryService;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class MedicalHistoryController {

    private MedicalHistoryService medicalHistoryService;

    @QueryMapping
    public String getMedicalHistoryByPatientId(String patientId) {
        return "Medical history for patient " + patientId;
    }

}
