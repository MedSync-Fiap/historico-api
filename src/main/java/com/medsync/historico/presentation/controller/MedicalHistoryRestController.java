package com.medsync.historico.presentation.controller;

import com.medsync.historico.application.dto.AppointmentEvent;
import com.medsync.historico.application.services.MedicalHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/medical-histories")
@RequiredArgsConstructor
public class MedicalHistoryRestController {

    private final MedicalHistoryService medicalHistoryService;

    @PostMapping
    public void createMedicalHistory(
            @RequestBody AppointmentEvent event
    ) {
        medicalHistoryService.addAppointmentInMedicalHistory(event);
    }

}
