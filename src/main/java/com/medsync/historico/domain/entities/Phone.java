package com.medsync.historico.domain.entities;

import com.medsync.historico.domain.enums.PhoneType;
import lombok.Data;

@Data
public class Phone {

    private PhoneType type;
    private String number;

}
