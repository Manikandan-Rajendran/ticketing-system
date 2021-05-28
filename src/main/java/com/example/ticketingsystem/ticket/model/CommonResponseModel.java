package com.example.ticketingsystem.ticket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonResponseModel {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String responseMessage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object response;
}
