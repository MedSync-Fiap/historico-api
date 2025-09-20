package com.medsync.historico.infrastructure.persistence.document;

import com.medsync.historico.infrastructure.persistence.document.embedded.Appointment;
import com.medsync.historico.infrastructure.persistence.document.embedded.PatientInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "medical_histories")
public class MedicalHistoryDocument {

    @Id
    private String id;

    @Field("patient")
    private PatientInfo patient;

    @Field("appointments")
    private List<Appointment> appointments;

}
