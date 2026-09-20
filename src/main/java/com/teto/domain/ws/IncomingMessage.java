package com.teto.domain.ws;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class IncomingMessage implements Serializable {
    private final String request;

    public IncomingMessage(String request) {
        this.request = request;
    }
}
