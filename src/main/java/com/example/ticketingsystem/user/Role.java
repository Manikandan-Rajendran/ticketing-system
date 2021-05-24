package com.example.ticketingsystem.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum Role {
    @JsonProperty("agent") AGENT(1,"Agent"),
    @JsonProperty("customer") CUSTOMER(2, "Customer");

    private final  Integer value;
    private final String key;

    Role(Integer val, String k){
        this.value = val;
        this.key = k;
    }

}
