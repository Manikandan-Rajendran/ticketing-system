package com.example.ticketingsystem.ticket.response;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table
public class Response {

    @Id
    @GeneratedValue
    private long id;

    private String content;

    private String author;
}
