package com.example.ticketingsystem.ticket.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum Priority {
    @JsonProperty("p0") P0(0,"P0"),
    @JsonProperty("p1") P1(1,"P1"),
    @JsonProperty("p2") P2(2,"P2"),
    @JsonProperty("p3") P3(3,"P3");

    private final Integer value;
    private final String key;

    Priority(Integer v, String k){
        this.value = v;
        this.key = k;
    }
}
