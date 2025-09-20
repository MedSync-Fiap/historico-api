package com.medsync.historico.domain.entities;

import lombok.Data;

import java.util.List;

@Data
public class MedicalHistory {

    private String id;
    private Patient patient;
    private List<Appointment> appointments;

}
