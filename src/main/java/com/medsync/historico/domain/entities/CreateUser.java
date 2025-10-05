package com.medsync.historico.domain.entities;

import com.medsync.historico.application.dto.AppointmentInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUser {

    private String id;
    private String name;
    private String email;
    private String role;

    public CreateUser(AppointmentInput input) {
        this.id = input.usuarioId();
        this.name = input.usuarioNome();
        this.email = input.usuarioEmail();
        this.role = input.usuarioRole();
    }

}
