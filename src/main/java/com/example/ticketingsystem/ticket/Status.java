package com.example.ticketingsystem.ticket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum Status {
    @JsonProperty("open") OPEN(0, "Open"),
    @JsonProperty("waiting-on-customer") WAITING_ON_CUSTOMER(1, "Waiting On Customer"),
    @JsonProperty("customer-responded") CUSTOMER_RESPONDED(2, "Customer Responded"),
    @JsonProperty("resolved") RESOLVED(3, "Resolved"),
    @JsonProperty("closed") CLOSED(4, "Closed");

    private final Integer value;
    private final String key;

    Status(Integer v, String s){
        this.value = v;
        this.key = s;
    }

}
