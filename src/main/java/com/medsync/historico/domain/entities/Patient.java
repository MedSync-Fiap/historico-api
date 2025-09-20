package com.medsync.historico.domain.entities;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Patient {

    private String id;
    private String name;
    private String email;
    private LocalDate dateOfBirth;
    private String cpf;
    private List<Phone> phones;

}
