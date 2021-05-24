package com.example.ticketingsystem.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Base64;

@Getter
public class UserRequest {

    private String basic;

    public String[] decode(){
        byte[] decodedBytes = Base64.getDecoder().decode(basic);
        String decodedString = new String(decodedBytes);
        return decodedString.split(":");
    }
}
