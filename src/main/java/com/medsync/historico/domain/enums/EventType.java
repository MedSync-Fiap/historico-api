package com.medsync.historico.domain.enums;

import lombok.Getter;

@Getter
public enum EventType {
    CREATION("CRIADA"),
    EDITION("EDITADA"),;

    private final String value;

    EventType(String value) {
        this.value = value;
    }

    public static EventType fromValue(String value) {
        for (EventType eventType : EventType.values()) {
            if (eventType.value.equalsIgnoreCase(value)) {
                return eventType;
            }
        }
        return null;
    }

}
