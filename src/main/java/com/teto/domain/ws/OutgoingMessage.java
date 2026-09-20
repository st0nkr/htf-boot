package com.teto.domain.ws;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OutgoingMessage {
    private final String response;

    public OutgoingMessage(String response) {
        this.response = response;
    }
}
