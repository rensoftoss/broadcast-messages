package com.rensoftoss.broadcastmessages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
public class ClientErrorInformation implements Serializable {
    private static final long serialVersionUID = 7789277000497157547L;

    @Getter @Setter private String message;
    @Getter @Setter private String detail;
    @Getter @Setter private String uri;
}
